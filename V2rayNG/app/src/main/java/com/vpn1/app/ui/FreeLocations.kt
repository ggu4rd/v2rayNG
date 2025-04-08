package com.vpn1.app.ui

import com.vpn1.app.model.Location
import com.vpn1.app.model.Host

val freeLocations = listOf(
    Location(
        country = "Netherlands",
        countryCode = "nl",
        hosts = listOf(
            Host("netherlands-free-1.cloudcdnflare.site", 443),
            Host("netherlands-free-2.cloudspacecdn.site", 443)
        ),
    ),
    Location(
        country = "USA West",
        countryCode = "usw",
        hosts = listOf(
            Host("usa-west-free-1.cloudcdnflare.site", 443),
            Host("usa-west-free-2.cloudspacecdn.site", 443)
        ),
    ),
    Location(
        country = "Australia",
        countryCode = "au",
        isPremium = true
    ),
    Location(
        country = "Brazil",
        countryCode = "br",
        isPremium = true
    ),
    Location(
        country = "Canada",
        countryCode = "ca",
        isPremium = true
    ),
    Location(
        country = "Chile",
        countryCode = "cl",
        isPremium = true
    ),
    Location(
        country = "France",
        countryCode = "fr",
        isPremium = true
    ),
    Location(
        country = "Germany",
        countryCode = "de",
        isPremium = true
    ),
    Location(
        country = "India",
        countryCode = "in",
        isPremium = true
    ),
    Location(
        country = "Japan",
        countryCode = "jp",
        isPremium = true
    ),
    Location(
        country = "South Korea",
        countryCode = "kr",
        isPremium = true
    ),
    Location(
        country = "Mexico",
        countryCode = "mx",
        isPremium = true
    ),
    Location(
        country = "Poland",
        countryCode = "pl",
        isPremium = true
    ),
    Location(
        country = "Singapore",
        countryCode = "sg",
        isPremium = true
    ),
    Location(
        country = "South Africa",
        countryCode = "za",
        isPremium = true
    ),
    Location(
        country = "Spain",
        countryCode = "es",
        isPremium = true
    ),
    Location(
        country = "Sweden",
        countryCode = "se",
        isPremium = true
    ),
    Location(
        country = "United Kingdom",
        countryCode = "uk",
        isPremium = true
    ),
    Location(
        country = "USA East",
        countryCode = "use",
        isPremium = true
    ),
    Location(
        country = "USA South",
        countryCode = "uss",
        isPremium = true
    )
)