package com.vpn1.app.api

import com.vpn1.app.model.UserDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

data class LoginRequest(
    val username: String,
    val password: String,
    val token: String? = null
)

data class SignUpRequest(
    val username: String,
    val password: String,
    val email: String
)

data class RefreshTokenResponse(
    val sessionAuthToken: String
)

interface ApiService {
    @Headers("Content-Type: application/json", "Client-Type: app")
    @POST("login/")
    suspend fun login(@Body request: LoginRequest): Response<UserDataResponse>

    @Headers("Content-Type: application/json", "Client-Type: app")
    @POST("signup/")
    suspend fun signUp(@Body request: SignUpRequest): Response<UserDataResponse>

    @Headers("Content-Type: application/json", "Client-Type: app")
    @POST("get_user_data/")
    suspend fun fetchUserData(
        @HeaderMap headers: Map<String, String>
    ): Response<UserDataResponse>

    @POST("refresh_token/")
    suspend fun refreshToken(
        @HeaderMap headers: Map<String, String>
    ): Response<RefreshTokenResponse>
}