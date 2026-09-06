package com.example.data.repository

import com.example.domain.models.*
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class FakeAuthRepository(
    private val store: AppDatabaseStore = AppDatabaseStore
) : AuthRepository {

    override fun getCurrentUser(): Flow<User?> = store.currentUser

    override suspend fun sendOtp(phoneNumber: String): Result<Boolean> {
        delay(600) // Realistic network delay simulation
        val cleanPhone = phoneNumber.replace(" ", "").replace("-", "")
        if (cleanPhone.length < 10) {
            return Result.failure(IllegalArgumentException("Enter a valid 10-digit mobile number."))
        }
        return Result.success(true)
    }

    override suspend fun verifyOtp(phoneNumber: String, otp: String): Result<User> {
        delay(700)
        if (otp.length != 6) {
            return Result.failure(IllegalArgumentException("Please enter the complete 6-digit OTP."))
        }
        if (otp == "000000") {
            return Result.failure(IllegalArgumentException("Incorrect OTP. Please try again."))
        }

        // Demo user check / Role detection
        val user = when {
            phoneNumber.endsWith("0000") || phoneNumber.contains("99999") -> {
                User(
                    uid = "usr_owner_001",
                    fullName = "Vikramaditya (Owner)",
                    phoneNumber = phoneNumber,
                    role = UserRole.OWNER,
                    accountStatus = AccountStatus.APPROVED,
                    area = "Corporate Head Office, Mumbai",
                    businessName = "Electricians Rewards Admin Desk"
                )
            }
            phoneNumber.endsWith("1111") || phoneNumber.contains("88888") -> {
                User(
                    uid = "usr_staff_001",
                    fullName = "Karan Shah (Verifier)",
                    phoneNumber = phoneNumber,
                    role = UserRole.STAFF,
                    accountStatus = AccountStatus.APPROVED,
                    area = "Western Region Hub, Mumbai",
                    businessName = "Electricians Verification Desk"
                )
            }
            phoneNumber.endsWith("2222") -> {
                User(
                    uid = "usr_pending_001",
                    fullName = "Suresh Patel",
                    phoneNumber = phoneNumber,
                    role = UserRole.ELECTRICIAN,
                    accountStatus = AccountStatus.PENDING,
                    area = "Thane West, Maharashtra"
                )
            }
            phoneNumber.endsWith("3333") -> {
                User(
                    uid = "usr_rejected_001",
                    fullName = "Rajesh Gupta",
                    phoneNumber = phoneNumber,
                    role = UserRole.ELECTRICIAN,
                    accountStatus = AccountStatus.REJECTED,
                    area = "Kalyan, Maharashtra",
                    rejectionReason = "Mismatch in trade license verification and tax documents."
                )
            }
            phoneNumber.endsWith("4444") -> {
                User(
                    uid = "usr_inactive_001",
                    fullName = "Mohan Lal",
                    phoneNumber = phoneNumber,
                    role = UserRole.ELECTRICIAN,
                    accountStatus = AccountStatus.INACTIVE,
                    area = "Navi Mumbai, Maharashtra"
                )
            }
            else -> {
                // Default registered electrician: Ramesh Kumar
                User(
                    uid = "usr_ramesh_101",
                    fullName = "Ramesh Kumar",
                    phoneNumber = phoneNumber,
                    role = UserRole.ELECTRICIAN,
                    accountStatus = AccountStatus.APPROVED,
                    electricianId = "ELC-2026-047",
                    area = "Andheri East, Mumbai",
                    businessName = "Ramesh Electrical Works"
                )
            }
        }
        store.setCurrentUser(user)
        return Result.success(user)
    }

    override suspend fun registerUser(
        fullName: String,
        phoneNumber: String,
        area: String,
        electricianId: String?,
        businessName: String?,
        referralCode: String?
    ): Result<RegistrationRequest> {
        delay(800)
        val request = RegistrationRequest(
            requestId = "req_${UUID.randomUUID().toString().take(6)}",
            fullName = fullName.trim(),
            phoneNumber = phoneNumber.trim(),
            area = area.trim(),
            businessName = businessName?.trim(),
            electricianId = electricianId?.trim(),
            referralCode = referralCode?.trim(),
            submissionDate = "Today",
            status = AccountStatus.PENDING
        )
        return Result.success(request)
    }

    override suspend fun verifyRegistrationOtp(phoneNumber: String, otp: String): Result<RegistrationRequest> {
        delay(700)
        if (otp != "123456" && otp.length != 6) {
            return Result.failure(IllegalArgumentException("Invalid verification code."))
        }
        val user = User(
            uid = "usr_pending_${UUID.randomUUID().toString().take(6)}",
            fullName = "Registered Applicant",
            phoneNumber = phoneNumber,
            role = UserRole.ELECTRICIAN,
            accountStatus = AccountStatus.PENDING,
            area = "India",
            createdAt = System.currentTimeMillis()
        )
        store.setCurrentUser(user)
        return Result.success(
            RegistrationRequest(
                requestId = "req_new_01",
                fullName = "Registered Applicant",
                phoneNumber = phoneNumber,
                area = "India",
                businessName = null,
                electricianId = null,
                referralCode = null,
                submissionDate = "Today",
                status = AccountStatus.PENDING
            )
        )
    }

    override suspend fun refreshUserStatus(): Result<User?> {
        delay(400)
        return Result.success(store.currentUser.value)
    }

    override suspend fun logout() {
        delay(200)
        store.setCurrentUser(null)
    }

    override suspend fun switchRoleForTesting(role: UserRole, status: AccountStatus) {
        val user = when (role) {
            UserRole.ELECTRICIAN -> User(
                uid = "usr_ramesh_101",
                fullName = "Ramesh Kumar",
                phoneNumber = "+919876543210",
                role = UserRole.ELECTRICIAN,
                accountStatus = status,
                electricianId = "ELC-2026-047",
                area = "Andheri East, Mumbai",
                businessName = "Ramesh Electrical Works",
                rejectionReason = if (status == AccountStatus.REJECTED) "Verification documents require additional trade stamp." else null
            )
            UserRole.STAFF -> User(
                uid = "usr_staff_001",
                fullName = "Karan Shah",
                phoneNumber = "+919811111111",
                role = UserRole.STAFF,
                accountStatus = status,
                area = "Western Verification Hub"
            )
            UserRole.OWNER -> User(
                uid = "usr_owner_001",
                fullName = "Vikramaditya (Owner)",
                phoneNumber = "+919800000000",
                role = UserRole.OWNER,
                accountStatus = status,
                area = "Corporate Head Office"
            )
        }
        store.setCurrentUser(user)
    }
}
