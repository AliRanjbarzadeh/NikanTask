package ir.aliranjbarzadeh.nikantask.core.interfaces

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface DetailViewModel<T> {
	val _items: MutableStateFlow<ResponseResult.Success<List<T>>?>
	val items: StateFlow<ResponseResult.Success<List<T>>?>

	fun setItems(result: ResponseResult.Success<List<T>>) {
		_items.value = result
	}
}