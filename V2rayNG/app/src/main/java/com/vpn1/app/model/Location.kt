package com.vpn1.app.model

data class Location(
    val country: String,
    val countryCode: String,
    val hosts: List<Host>? = null,
    val isPremium: Boolean = false
)