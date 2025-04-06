package ir.aliranjbarzadeh.nikantask.data.repositories

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(private val dataSource: AuthDataSource) : AuthRepository {
	override fun initUser(): Flow<ResponseResult<Boolean>> = dataSource.initUser()
	override fun login(username: String, password: String): Flow<ResponseResult<String?>> = dataSource.login(username, password)
	override fun logout(token: String): Flow<ResponseResult<Boolean>> = dataSource.logout(token)
}