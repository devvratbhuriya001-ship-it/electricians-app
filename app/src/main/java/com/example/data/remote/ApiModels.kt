package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendOtpRequest(
    @Json(name = "phoneNumber") val phoneNumber: String,
    @Json(name = "isRegistration") val isRegistration: Boolean = false
)

@JsonClass(generateAdapter = true)
data class SendOtpResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "sessionId") val sessionId: String? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "notRegistered") val notRegistered: Boolean? = null,
    @Json(name = "alreadyRegistered") val alreadyRegistered: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class VerifyOtpRequest(
    @Json(name = "phoneNumber") val phoneNumber: String,
    @Json(name = "otp") val otp: String,
    @Json(name = "sessionId") val sessionId: String
)

@JsonClass(generateAdapter = true)
data class UserProfileDto(
    @Json(name = "uid") val uid: String,
    @Json(name = "fullName") val fullName: String,
    @Json(name = "phoneNumber") val phoneNumber: String,
    @Json(name = "role") val role: String,
    @Json(name = "accountStatus") val accountStatus: String,
    @Json(name = "electricianId") val electricianId: String? = null,
    @Json(name = "area") val area: String? = null,
    @Json(name = "businessName") val businessName: String? = null,
    @Json(name = "profileImageUrl") val profileImageUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class VerifyOtpResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "customToken") val customToken: String? = null,
    @Json(name = "user") val user: UserProfileDto? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "notRegistered") val notRegistered: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class RegisterUserRequest(
    @Json(name = "fullName") val fullName: String,
    @Json(name = "phoneNumber") val phoneNumber: String,
    @Json(name = "area") val area: String,
    @Json(name = "electricianId") val electricianId: String? = null,
    @Json(name = "businessName") val businessName: String? = null,
    @Json(name = "referralCode") val referralCode: String? = null
)

@JsonClass(generateAdapter = true)
data class RegisterUserResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "sessionId") val sessionId: String? = null,
    @Json(name = "message") val message: String? = null
)

@JsonClass(generateAdapter = true)
data class VerifyRegistrationOtpRequest(
    @Json(name = "otp") val otp: String,
    @Json(name = "sessionId") val sessionId: String
)

@JsonClass(generateAdapter = true)
data class VerifyRegistrationOtpResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "customToken") val customToken: String? = null,
    @Json(name = "registrationId") val registrationId: String? = null,
    @Json(name = "accountStatus") val accountStatus: String? = null,
    @Json(name = "message") val message: String? = null
)

@JsonClass(generateAdapter = true)
data class CheckUserAccessRequest(
    @Json(name = "phoneNumber") val phoneNumber: String
)

@JsonClass(generateAdapter = true)
data class CheckUserAccessResponse(
    @Json(name = "exists") val exists: Boolean,
    @Json(name = "accountStatus") val accountStatus: String? = null
)
