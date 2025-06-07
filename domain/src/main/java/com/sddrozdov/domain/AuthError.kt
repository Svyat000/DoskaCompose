package com.sddrozdov.domain

sealed class AuthError : Exception() {
    object EmptyCredentials : AuthError()
    object UserNotFound : AuthError()
    object EmailAlreadyInUse : AuthError()
    data class UnknownError(override val cause: Throwable? = null) : AuthError()
}