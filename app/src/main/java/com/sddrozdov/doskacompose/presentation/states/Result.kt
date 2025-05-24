package com.sddrozdov.doskacompose.presentation.states

//sealed class Result<out T> {
//    data class Success<out T>(val data: T) : Result<T>()
//    data class Failure(val exception: Exception) : Result<Nothing>()
//
//    val isSuccess: Boolean get() = this is Success<T>
//    val isFailure: Boolean get() = this is Failure
//
//    fun exceptionOrNull(): Exception? = (this as? Failure)?.exception
//
//    companion object {
//        fun <T> success(data: T): Result<T> = Success(data)
//        fun failure(exception: Exception): Result<Nothing> = Failure(exception)
//    }
//}