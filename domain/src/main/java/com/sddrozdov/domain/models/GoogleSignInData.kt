package com.sddrozdov.domain.models

data class GoogleSignInData(
    val idToken: String,
    val accessToken: String? = null
)

