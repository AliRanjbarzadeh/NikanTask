package ir.aliranjbarzadeh.nikantask.core.interfaces

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface SeedViewModel {
	val _seed: MutableStateFlow<ResponseResult.Success<Boolean>?>
	val seed: StateFlow<ResponseResult.Success<Boolean>?>
}