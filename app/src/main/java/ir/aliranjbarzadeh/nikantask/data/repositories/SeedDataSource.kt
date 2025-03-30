package ir.aliranjbarzadeh.nikantask.data.repositories

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.Flow

interface SeedDataSource {
	fun seed(): Flow<ResponseResult<Boolean>>
}