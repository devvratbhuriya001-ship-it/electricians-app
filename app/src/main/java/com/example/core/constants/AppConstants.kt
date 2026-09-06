package com.example.core.constants

object AppConstants {
    const val APP_NAME = "Electricians App"
    const val TAGLINE = "Powering Progress, Lighting Lives"
    const val SCHEME_NAME = "Electrician Scheme"
    const val SUPPORT_PHONE = "+91 1800 200 4567"
    const val SUPPORT_EMAIL = "support@electriciansapp.com"
    const val DEFAULT_COUNTRY_CODE = "+91"
    const val OTP_LENGTH = 6
    const val RESEND_OTP_SECONDS = 30

    // Cloud Functions Endpoints & Firestore Collections
    const val FIRESTORE_USERS_COLLECTION = "users"
    const val FIRESTORE_REGISTRATIONS_COLLECTION = "registration_requests"
    const val FIRESTORE_AUDIT_LOGS_COLLECTION = "audit_logs"

    // Default Cloud Functions base URL (can be customized via BuildConfig or backend deployment)
    const val DEFAULT_FUNCTIONS_BASE_URL = "https://asia-south1-electricians-app.cloudfunctions.net/"
}
