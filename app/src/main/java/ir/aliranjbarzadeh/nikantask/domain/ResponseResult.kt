package ir.aliranjbarzadeh.nikantask.domain

import ir.aliranjbarzadeh.nikantask.data.models.Error as BaseError

sealed class ResponseResult<out T> {
	data class Success<T>(val data: T) : ResponseResult<T>()
	data class Error(val data: BaseError) : ResponseResult<Nothing>()
}