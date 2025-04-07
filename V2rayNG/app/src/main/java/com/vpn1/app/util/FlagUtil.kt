package com.vpn1.app.util

import com.vpn1.app.R

fun getFlag(countryCode: String): Int {
    val flagMap = mapOf(
        "au" to R.drawable.flag_au,
        "br" to R.drawable.flag_br,
        "ca" to R.drawable.flag_ca,
        "cl" to R.drawable.flag_cl,
        "fr" to R.drawable.flag_fr,
        "de" to R.drawable.flag_de,
        "in" to R.drawable.flag_in,
        "jp" to R.drawable.flag_jp,
        "kr" to R.drawable.flag_kr,
        "mx" to R.drawable.flag_mx,
        "nl" to R.drawable.flag_nl,
        "pl" to R.drawable.flag_pl,
        "sg" to R.drawable.flag_sg,
        "za" to R.drawable.flag_za,
        "es" to R.drawable.flag_es,
        "se" to R.drawable.flag_se,
        "uk" to R.drawable.flag_uk,
        "use" to R.drawable.flag_us,
        "uss" to R.drawable.flag_us,
        "usw" to R.drawable.flag_us
    )
    return flagMap[countryCode.lowercase()] ?: R.drawable.flag_us
}