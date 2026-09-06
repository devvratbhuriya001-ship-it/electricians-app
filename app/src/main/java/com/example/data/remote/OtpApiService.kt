package com.example.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OtpApiService {

    @POST("sendOtp")
    suspend fun sendOtp(
        @Body request: SendOtpRequest
    ): Response<SendOtpResponse>

    @POST("verifyOtp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<VerifyOtpResponse>

    @POST("registerUser")
    suspend fun registerUser(
        @Body request: RegisterUserRequest
    ): Response<RegisterUserResponse>

    @POST("verifyRegistrationOtp")
    suspend fun verifyRegistrationOtp(
        @Body request: VerifyRegistrationOtpRequest
    ): Response<VerifyRegistrationOtpResponse>

    @POST("checkUserAccess")
    suspend fun checkUserAccess(
        @Body request: CheckUserAccessRequest
    ): Response<CheckUserAccessResponse>
}
