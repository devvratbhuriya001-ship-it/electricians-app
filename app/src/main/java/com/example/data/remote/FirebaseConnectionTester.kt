package com.example.data.remote

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

sealed class FirebaseConnectionState {
    object Idle : FirebaseConnectionState()
    object Testing : FirebaseConnectionState()
    data class Success(
        val documentId: String,
        val collectionName: String,
        val projectId: String,
        val appName: String,
        val packageName: String,
        val writeConfirmed: Boolean,
        val readConfirmed: Boolean,
        val connectionStatus: String
    ) : FirebaseConnectionState()
    data class Failed(
        val error: String,
        val details: String? = null
    ) : FirebaseConnectionState()
}

object FirebaseConnectionTester {
    private const val TAG = "FirebaseConnTest"
    const val TEST_COLLECTION = "firebase_connection_test"

    private val _connectionState = MutableStateFlow<FirebaseConnectionState>(FirebaseConnectionState.Idle)
    val connectionState: StateFlow<FirebaseConnectionState> = _connectionState.asStateFlow()

    suspend fun runConnectionTest(): FirebaseConnectionState = withContext(Dispatchers.IO) {
        _connectionState.value = FirebaseConnectionState.Testing
        try {
            // 1. Verify Firebase App Initialization
            val app = FirebaseApp.getInstance()
            val projectId = app.options.projectId ?: "electricians-c"
            val pkgName = app.applicationContext.packageName

            Log.d(TAG, "FirebaseApp initialized for project: $projectId, package: $pkgName")

            // 2. Verify Core SDK Initializations
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            val functions = FirebaseFunctions.getInstance()

            Log.d(TAG, "FirebaseAuth, FirebaseFirestore, and FirebaseFunctions initialized successfully")

            // 3. Perform REAL Cloud Firestore WRITE
            val docRef = firestore.collection(TEST_COLLECTION).document()
            val docId = docRef.id

            val testPayload = hashMapOf(
                "appName" to "Electricians App",
                "connectionStatus" to "CONNECTED",
                "testType" to "FIRESTORE_CONNECTION_TEST",
                "packageName" to "electricians_01.com",
                "createdAt" to FieldValue.serverTimestamp()
            )

            Log.d(TAG, "Writing test document to $TEST_COLLECTION/$docId ...")
            docRef.set(testPayload).await()
            Log.d(TAG, "WRITE succeeded for document: $docId")

            // 4. Perform REAL Cloud Firestore READ back from the same document
            Log.d(TAG, "Reading back document from $TEST_COLLECTION/$docId ...")
            val snapshot = docRef.get().await()

            if (!snapshot.exists()) {
                val errorMsg = "Document write was reported, but document was not found when read back."
                val failedState = FirebaseConnectionState.Failed(error = "Firebase Connection Failed", details = errorMsg)
                _connectionState.value = failedState
                return@withContext failedState
            }

            val fetchedStatus = snapshot.getString("connectionStatus")
            val fetchedAppName = snapshot.getString("appName") ?: "Electricians App"
            val fetchedPackage = snapshot.getString("packageName") ?: "electricians_01.com"

            if (fetchedStatus != "CONNECTED") {
                val errorMsg = "Field 'connectionStatus' expected 'CONNECTED' but found '$fetchedStatus'."
                val failedState = FirebaseConnectionState.Failed(error = "Firebase Connection Failed", details = errorMsg)
                _connectionState.value = failedState
                return@withContext failedState
            }

            Log.d(TAG, "READ succeeded! Document verified: connectionStatus=$fetchedStatus")

            val successState = FirebaseConnectionState.Success(
                documentId = docId,
                collectionName = TEST_COLLECTION,
                projectId = projectId,
                appName = fetchedAppName,
                packageName = fetchedPackage,
                writeConfirmed = true,
                readConfirmed = true,
                connectionStatus = fetchedStatus
            )
            _connectionState.value = successState
            successState
        } catch (e: Exception) {
            val safeMessage = sanitizeError(e)
            Log.e(TAG, "Connection test failed: ${e.javaClass.simpleName}: ${e.message}", e)
            val failedState = FirebaseConnectionState.Failed(
                error = "Firebase Connection Failed",
                details = safeMessage
            )
            _connectionState.value = failedState
            failedState
        }
    }

    private fun sanitizeError(e: Exception): String {
        val msg = e.localizedMessage ?: e.message ?: "Unknown database error"
        return when {
            msg.contains("PERMISSION_DENIED", ignoreCase = true) ->
                "Firestore permission denied. In Firebase Console > Firestore > Rules, add: match /firebase_connection_test/{testDocId} { allow read, write: if true; }"
            msg.contains("UNAVAILABLE", ignoreCase = true) ->
                "Cloud Firestore server unavailable. Please check your internet connection."
            msg.contains("NOT_FOUND", ignoreCase = true) ->
                "Firestore database not found. Please create Cloud Firestore in the Firebase Console."
            else -> msg
        }
    }
}
