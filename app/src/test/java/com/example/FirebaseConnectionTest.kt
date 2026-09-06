package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.remote.FirebaseConnectionState
import com.example.data.remote.FirebaseConnectionTester
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class FirebaseConnectionTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        val hasDefaultApp = FirebaseApp.getApps(context).any { it.name == FirebaseApp.DEFAULT_APP_NAME }
        if (!hasDefaultApp) {
            try {
                val options = FirebaseOptions.Builder()
                    .setProjectId("electricians-c")
                    .setApplicationId("1:582772904838:android:53ba0cdf4947572d861207")
                    .setApiKey("AIzaSyDJeZsp27yj1bWsocsTYeeMLYnG6USo7fg")
                    .setStorageBucket("electricians-c.firebasestorage.app")
                    .build()
                FirebaseApp.initializeApp(context, options)
            } catch (e: Exception) {
                // Ignore if already initialized in concurrent test runner
            }
        }
    }

    @Test
    fun testFirebaseAppInitialization() {
        val app = FirebaseApp.getInstance()
        assertNotNull("FirebaseApp should be initialized", app)
        assertEquals("electricians-c", app.options.projectId)
        assertEquals("1:582772904838:android:53ba0cdf4947572d861207", app.options.applicationId)
        assertEquals("AIzaSyDJeZsp27yj1bWsocsTYeeMLYnG6USo7fg", app.options.apiKey)
        assertEquals("electricians-c.firebasestorage.app", app.options.storageBucket)
    }

    @Test
    fun testFirebaseServicesInitialization() {
        val app = FirebaseApp.getInstance()
        val auth = FirebaseAuth.getInstance(app)
        val firestore = FirebaseFirestore.getInstance(app)
        val functions = FirebaseFunctions.getInstance(app)

        assertNotNull("FirebaseAuth instance should not be null", auth)
        assertNotNull("FirebaseFirestore instance should not be null", firestore)
        assertNotNull("FirebaseFunctions instance should not be null", functions)
    }

    @Test
    fun testConnectionTestPayloadContract() {
        assertEquals("firebase_connection_test", FirebaseConnectionTester.TEST_COLLECTION)
    }
}
