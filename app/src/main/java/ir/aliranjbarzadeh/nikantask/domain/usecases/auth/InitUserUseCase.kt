package ir.aliranjbarzadeh.nikantask.domain.usecases.auth

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InitUserUseCase @Inject constructor(private val repository: AuthRepository) {
	operator fun invoke(): Flow<ResponseResult<Boolean>> = repository.initUser()
}