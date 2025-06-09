package com.sddrozdov.domain.useCase

sealed class AuthError(message: String?=null) : Throwable(message){
    object EmailAlreadyInUse : AuthError()
    data class Unknown(val throwable: Throwable) : AuthError()
}