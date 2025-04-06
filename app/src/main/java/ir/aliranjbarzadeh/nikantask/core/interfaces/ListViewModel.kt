package ir.aliranjbarzadeh.nikantask.core.interfaces

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ListViewModel<T> {
	val limit: Int
	var offset: Int
	var allItemsLoaded: Boolean

	val _items: MutableStateFlow<ResponseResult.Success<List<T>>?>
	val items: StateFlow<ResponseResult.Success<List<T>>?>

	val _isEmptyList: MutableStateFlow<Boolean?>
	val isEmptyList: StateFlow<Boolean?>

	fun setItems(result: ResponseResult.Success<List<T>>, isNextPage: Boolean = false) {
		allItemsLoaded = result.data.size < limit
		_items.value = result
		if (!isNextPage && offset == 0)
			_isEmptyList.value = result.data.isEmpty()
	}
}