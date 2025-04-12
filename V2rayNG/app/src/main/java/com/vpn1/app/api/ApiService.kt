package com.vpn1.app.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
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

interface ApiService {
    @Headers("Content-Type: application/json", "Client-Type: app")
    @POST("login/")
    suspend fun login(@Body request: LoginRequest): Response<ResponseBody>

    @Headers("Content-Type: application/json", "Client-Type: app")
    @POST("signup/")
    suspend fun signUp(@Body request: SignUpRequest): Response<ResponseBody>
}