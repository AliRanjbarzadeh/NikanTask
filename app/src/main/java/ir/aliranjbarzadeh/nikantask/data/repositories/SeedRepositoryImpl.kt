package ir.aliranjbarzadeh.nikantask.data.repositories

import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.SeedRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeedRepositoryImpl @Inject constructor(private val dataSource: SeedDataSource) : SeedRepository {
	override fun seed(): Flow<ResponseResult<Boolean>> = dataSource.seed()
}