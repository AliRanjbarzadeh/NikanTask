package ir.aliranjbarzadeh.nikantask.domain

import ir.aliranjbarzadeh.nikantask.data.models.Error as ErrorModel

sealed class ResponseResult<T> {
	data class Success<T>(val data: T) : ResponseResult<T>()
	data class Error<T>(val data: ErrorModel) : ResponseResult<T>()
}