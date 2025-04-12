package com.vpn1.app.model

data class UserDataResponse(
    val username: String,
    val email: String,
    val isPremium: Boolean,
    val sessionAuthToken: String,
    val locations: List<Location>
)

data class ErrorResponse(
    val error: String,
    val code: Int? = null
)

