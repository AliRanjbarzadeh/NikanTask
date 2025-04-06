package ir.aliranjbarzadeh.nikantask.domain.usecases

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.SeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeedUseCase @Inject constructor(private val repository: SeedRepository) {
	operator fun invoke(): Flow<ResponseResult<Boolean>> = repository.seed()
}