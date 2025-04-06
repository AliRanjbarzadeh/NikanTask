package ir.aliranjbarzadeh.nikantask.domain.repositories

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
	fun initUser(): Flow<ResponseResult<Boolean>>
	fun login(username: String, password: String): Flow<ResponseResult<String?>>
	fun logout(token: String): Flow<ResponseResult<Boolean>>
}