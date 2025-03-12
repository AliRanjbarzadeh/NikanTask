package ir.aliranjbarzadeh.nikantask.domain.network

import ir.aliranjbarzadeh.nikantask.data.models.Error as BaseError

sealed class NetworkResult<T> {
	data class Success<T>(val data: T) : NetworkResult<T>()
	data class Error<T>(val data: BaseError) : NetworkResult<T>()
}