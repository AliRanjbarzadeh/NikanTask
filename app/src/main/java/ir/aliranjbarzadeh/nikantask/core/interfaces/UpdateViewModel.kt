package ir.aliranjbarzadeh.nikantask.core.interfaces

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface UpdateViewModel {
	val _update: MutableStateFlow<ResponseResult<Long>?>
	val update: StateFlow<ResponseResult<Long>?>
}