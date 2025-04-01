package com.vpn1.app.dto

data class ConfigResult(
    var status: Boolean,
    var guid: String? = null,
    var content: String = "",
    var domainPort: String? = null,
)

