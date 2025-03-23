package ir.aliranjbarzadeh.nikantask.core.interfaces

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface StoreViewModel<T> {
	val _store: MutableStateFlow<ResponseResult.Success<T>?>
	val store: StateFlow<ResponseResult.Success<T>?>

	fun setItem(result: ResponseResult.Success<T>) {
		_store.value = result
	}
}