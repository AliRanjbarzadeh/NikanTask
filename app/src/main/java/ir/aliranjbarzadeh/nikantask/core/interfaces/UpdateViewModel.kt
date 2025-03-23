package ir.aliranjbarzadeh.nikantask.core.interfaces

import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface UpdateViewModel<T> {
	val _update: MutableStateFlow<ResponseResult.Success<T>?>
	val update: StateFlow<ResponseResult.Success<T>?>

	fun updateItem(result: ResponseResult.Success<T>) {
		_update.value = result
	}
}