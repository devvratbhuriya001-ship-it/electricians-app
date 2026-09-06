package com.example.data.remote

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

object FirebaseAppCheckManager {
    private const val TAG = "AppCheckManager"

    fun initialize(context: Context) {
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                Log.d(TAG, "FirebaseApp not yet initialized; skipping App Check.")
                return
            }

            val appCheck = FirebaseAppCheck.getInstance()
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Initializing Firebase App Check with Debug provider")
                appCheck.installAppCheckProviderFactory(
                    DebugAppCheckProviderFactory.getInstance()
                )
            } else {
                Log.d(TAG, "Initializing Firebase App Check with Play Integrity provider")
                appCheck.installAppCheckProviderFactory(
                    PlayIntegrityAppCheckProviderFactory.getInstance()
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "Firebase App Check initialization deferred: ${e.message}")
        }
    }
}
