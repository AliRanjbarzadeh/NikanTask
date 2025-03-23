package ir.aliranjbarzadeh.nikantask.core.interfaces

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface DestroyViewModel {
	val _destroy: MutableStateFlow<Int>
	val destroy: StateFlow<Int>

	fun destroyed(position: Int) {
		_destroy.value = position
	}
}