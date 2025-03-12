package ir.aliranjbarzadeh.nikantask.core.interfaces

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface StoreViewModel {
	val _store: MutableStateFlow<ResponseResult<Long>?>
	val store: StateFlow<ResponseResult<Long>?>
}