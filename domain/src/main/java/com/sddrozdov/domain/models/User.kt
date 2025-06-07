package com.sddrozdov.domain.models

data class User(
    val id: String,
    val email: String,
    val isEmailVerified: Boolean
)