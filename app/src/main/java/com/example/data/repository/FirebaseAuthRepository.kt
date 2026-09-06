package com.example.data.repository

import android.util.Log
import com.example.BuildConfig
import com.example.core.constants.AppConstants
import com.example.data.remote.*
import com.example.domain.models.*
import com.example.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class UnregisteredUserException(message: String) : Exception(message)

class FirebaseAuthRepository(
    private val apiService: OtpApiService = NetworkClient.createOtpApiService(),
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) : AuthRepository {

    private val tag = "FirebaseAuthRepository"

    private val _currentUser = MutableStateFlow<User?>(null)
    override fun getCurrentUser(): Flow<User?> = _currentUser.asStateFlow()

    // Temporary in-memory session pointers (server holds the secret state)
    private var lastOtpSessionId: String? = null
    private var lastRegistrationSessionId: String? = null
    private var pendingRegistrationDraft: RegistrationRequest? = null

    // Safe Firebase references
    private val firebaseAuth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Log.w(tag, "FirebaseAuth unavailable: ${e.message}")
            null
        }
    }

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.w(tag, "FirebaseFirestore unavailable: ${e.message}")
            null
        }
    }

    init {
        // Observe persistent Firebase session on app launch
        listenToAuthSession()
    }

    private fun listenToAuthSession() {
        val auth = firebaseAuth ?: return
        auth.addAuthStateListener { fa ->
            val user = fa.currentUser
            if (user != null) {
                scope.launch {
                    loadUserProfileFromFirestore(user.uid)
                }
            } else {
                _currentUser.value = null
            }
        }
    }

    private suspend fun loadUserProfileFromFirestore(uid: String): User? {
        val db = firestore ?: return _currentUser.value
        return try {
            val doc = db.collection(AppConstants.FIRESTORE_USERS_COLLECTION)
                .document(uid)
                .get()
                .await()

            if (doc.exists()) {
                val data = doc.data ?: emptyMap()
                val roleStr = (data["role"] as? String)?.uppercase() ?: "ELECTRICIAN"
                val statusStr = (data["accountStatus"] as? String)?.uppercase() ?: "PENDING"

                val role = try {
                    UserRole.valueOf(roleStr)
                } catch (e: Exception) {
                    UserRole.ELECTRICIAN
                }

                val status = try {
                    AccountStatus.valueOf(statusStr)
                } catch (e: Exception) {
                    AccountStatus.PENDING
                }

                val user = User(
                    uid = uid,
                    fullName = data["fullName"] as? String ?: "Electrician User",
                    phoneNumber = data["phoneNumber"] as? String ?: "",
                    role = role,
                    accountStatus = status,
                    electricianId = data["electricianId"] as? String,
                    area = data["area"] as? String ?: "India",
                    businessName = data["businessName"] as? String,
                    profileImageUrl = data["profileImageUrl"] as? String,
                    rejectionReason = data["rejectionReason"] as? String
                )
                _currentUser.value = user
                user
            } else {
                _currentUser.value
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to load Firestore profile for $uid: ${e.message}")
            _currentUser.value
        }
    }

    override suspend fun sendOtp(phoneNumber: String): Result<Boolean> {
        val cleanPhone = phoneNumber.replace("+91", "").replace(" ", "").replace("-", "")
        if (cleanPhone.length != 10) {
            return Result.failure(IllegalArgumentException("Please enter a valid 10-digit mobile number."))
        }

        return try {
            // Production Call to Firebase Cloud Function (which invokes 2Factor.in SMS OTP API)
            val response = apiService.sendOtp(
                SendOtpRequest(phoneNumber = cleanPhone, isRegistration = false)
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    lastOtpSessionId = body.sessionId
                    Result.success(true)
                } else if (body?.notRegistered == true) {
                    Result.failure(UnregisteredUserException(
                        body.message ?: "This mobile number is not registered. Please register first."
                    ))
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to deliver SMS verification code."))
                }
            } else if (response.code() == 404) {
                Result.failure(UnregisteredUserException("This mobile number is not registered. Please register first."))
            } else if (response.code() == 429) {
                Result.failure(Exception("Too many requests. Please wait before requesting another OTP."))
            } else {
                val errorBody = response.errorBody()?.string()
                val message = if (errorBody?.contains("not registered", ignoreCase = true) == true) {
                    "This mobile number is not registered. Please register first."
                } else {
                    "Unable to deliver SMS OTP. Please try again."
                }
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Log.e(tag, "sendOtp network error: ${e.message}")
            Result.failure(Exception("Unable to contact verification server. Please check your internet connection."))
        }
    }

    override suspend fun verifyOtp(phoneNumber: String, otp: String): Result<User> {
        val cleanOtp = otp.trim()
        if (cleanOtp.length != 6 || !cleanOtp.all { it.isDigit() }) {
            return Result.failure(IllegalArgumentException("Please enter the complete 6-digit numeric OTP."))
        }

        val cleanPhone = phoneNumber.replace("+91", "").replace(" ", "").replace("-", "")
        val sessionId = lastOtpSessionId
        if (sessionId.isNullOrEmpty()) {
            return Result.failure(Exception("Verification session expired. Please request a new OTP."))
        }

        return try {
            // Call Cloud Function to verify OTP via 2Factor.in and get Firebase Custom Token
            val response = apiService.verifyOtp(
                VerifyOtpRequest(phoneNumber = cleanPhone, otp = cleanOtp, sessionId = sessionId)
            )

            if (response.isSuccessful && response.body()?.success == true) {
                val body = response.body()!!
                val customToken = body.customToken

                if (!customToken.isNullOrEmpty() && firebaseAuth != null) {
                    // Sign in to Firebase Auth using Firebase Custom Token
                    val authResult = firebaseAuth!!.signInWithCustomToken(customToken).await()
                    val uid = authResult.user?.uid ?: body.user?.uid ?: ""

                    // Load user profile from Firestore users/{uid}
                    val profile = loadUserProfileFromFirestore(uid) ?: User(
                        uid = uid,
                        fullName = body.user?.fullName ?: "Electrician",
                        phoneNumber = "+91$cleanPhone",
                        role = try { UserRole.valueOf(body.user?.role ?: "ELECTRICIAN") } catch (e: Exception) { UserRole.ELECTRICIAN },
                        accountStatus = try { AccountStatus.valueOf(body.user?.accountStatus ?: "PENDING") } catch (e: Exception) { AccountStatus.PENDING },
                        area = body.user?.area ?: "India"
                    )
                    _currentUser.value = profile
                    Result.success(profile)
                } else if (body.user != null) {
                    val user = User(
                        uid = body.user.uid,
                        fullName = body.user.fullName,
                        phoneNumber = "+91$cleanPhone",
                        role = try { UserRole.valueOf(body.user.role) } catch (e: Exception) { UserRole.ELECTRICIAN },
                        accountStatus = try { AccountStatus.valueOf(body.user.accountStatus) } catch (e: Exception) { AccountStatus.PENDING },
                        area = body.user.area ?: "India"
                    )
                    _currentUser.value = user
                    Result.success(user)
                } else {
                    Result.failure(Exception("Authentication failed: invalid server credentials."))
                }
            } else {
                val message = response.body()?.message ?: "The OTP you entered is incorrect."
                Result.failure(Exception(message))
            }
        } catch (e: Exception) {
            Log.e(tag, "verifyOtp error: ${e.message}")
            Result.failure(Exception(e.localizedMessage ?: "Verification failed. Please check your internet connection."))
        }
    }

    override suspend fun registerUser(
        fullName: String,
        phoneNumber: String,
        area: String,
        electricianId: String?,
        businessName: String?,
        referralCode: String?
    ): Result<RegistrationRequest> {
        val cleanPhone = phoneNumber.replace("+91", "").replace(" ", "").replace("-", "")

        val draft = RegistrationRequest(
            requestId = "req_${UUID.randomUUID().toString().take(8)}",
            fullName = fullName.trim(),
            phoneNumber = "+91$cleanPhone",
            area = area.trim(),
            businessName = businessName?.trim(),
            electricianId = electricianId?.trim(),
            referralCode = referralCode?.trim(),
            submissionDate = "Today",
            status = AccountStatus.PENDING
        )
        pendingRegistrationDraft = draft

        return try {
            val response = apiService.registerUser(
                RegisterUserRequest(
                    fullName = fullName.trim(),
                    phoneNumber = cleanPhone,
                    area = area.trim(),
                    electricianId = electricianId?.trim(),
                    businessName = businessName?.trim(),
                    referralCode = referralCode?.trim()
                )
            )

            if (response.isSuccessful && response.body()?.success == true) {
                lastRegistrationSessionId = response.body()?.sessionId
                Result.success(draft)
            } else {
                val msg = response.body()?.message ?: "Unable to register. Mobile number may already exist."
                Result.failure(Exception(msg))
            }
        } catch (e: Exception) {
            Log.e(tag, "registerUser network error: ${e.message}")
            Result.failure(Exception("Registration service unavailable. Check network connection."))
        }
    }

    override suspend fun verifyRegistrationOtp(phoneNumber: String, otp: String): Result<RegistrationRequest> {
        val cleanOtp = otp.trim()
        val draft = pendingRegistrationDraft ?: RegistrationRequest(
            requestId = "req_${UUID.randomUUID().toString().take(8)}",
            fullName = "Applicant Electrician",
            phoneNumber = phoneNumber,
            area = "India",
            businessName = null,
            electricianId = null,
            referralCode = null,
            submissionDate = "Today",
            status = AccountStatus.PENDING
        )

        val sessionId = lastRegistrationSessionId
        if (sessionId.isNullOrEmpty()) {
            return Result.failure(Exception("Registration session expired. Please restart registration."))
        }

        return try {
            val response = apiService.verifyRegistrationOtp(
                VerifyRegistrationOtpRequest(otp = cleanOtp, sessionId = sessionId)
            )

            if (response.isSuccessful && response.body()?.success == true) {
                val customToken = response.body()?.customToken
                if (!customToken.isNullOrEmpty() && firebaseAuth != null) {
                    firebaseAuth!!.signInWithCustomToken(customToken).await()
                }

                val pendingUser = User(
                    uid = response.body()?.registrationId ?: draft.requestId,
                    fullName = draft.fullName,
                    phoneNumber = draft.phoneNumber,
                    role = UserRole.ELECTRICIAN,
                    accountStatus = AccountStatus.PENDING,
                    area = draft.area,
                    businessName = draft.businessName,
                    electricianId = draft.electricianId
                )
                _currentUser.value = pendingUser
                Result.success(draft)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Invalid registration OTP code."))
            }
        } catch (e: Exception) {
            Log.e(tag, "verifyRegistrationOtp error: ${e.message}")
            Result.failure(Exception("Verification failed. Please check your connection and try again."))
        }
    }

    override suspend fun refreshUserStatus(): Result<User?> {
        val auth = firebaseAuth
        val currentUser = auth?.currentUser

        if (currentUser == null) {
            // Check if in-memory user exists (e.g. dev mode)
            return Result.success(_currentUser.value)
        }

        return try {
            val user = loadUserProfileFromFirestore(currentUser.uid)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        try {
            firebaseAuth?.signOut()
        } catch (e: Exception) {
            Log.e(tag, "Error signing out: ${e.message}")
        }
        _currentUser.value = null
        lastOtpSessionId = null
        lastRegistrationSessionId = null
        pendingRegistrationDraft = null
    }

    override suspend fun switchRoleForTesting(role: UserRole, status: AccountStatus) {
        if (!BuildConfig.DEBUG) return
        val current = _currentUser.value ?: User(
            uid = "usr_dev_switch",
            fullName = "Test User",
            phoneNumber = "+919876543210",
            role = role,
            accountStatus = status,
            area = "Test Area"
        )
        _currentUser.value = current.copy(role = role, accountStatus = status)
    }
}
