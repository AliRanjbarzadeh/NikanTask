package ir.aliranjbarzadeh.nikantask.domain.repositories

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.Flow

interface SeedRepository {
	fun seed(): Flow<ResponseResult<Boolean>>
}