package com.sddrozdov.domain.models

data class User(
    val uid: String? = null,
    val email: String? = null,
    val isEmailVerified: Boolean? = false,
    val isAnonymous: Boolean? = false,
    val displayName: String? = null,
    val phoneNumber: String? = null,
    val photoUrl: String? = null,
)