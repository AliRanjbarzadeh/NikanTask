package ir.aliranjbarzadeh.nikantask.data.repositories

import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.CustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepositoryImpl @Inject constructor(private val dataSource: CustomerDataSource) : CustomerRepository {
	override fun list(): Flow<ResponseResult<List<Customer>>> = dataSource.list()

	override fun store(customer: Customer): Flow<ResponseResult<Long>> = dataSource.store(customer)

	override fun update(customer: Customer): Flow<ResponseResult<Int>> = dataSource.update(customer)

	override fun destroy(customer: Customer): Flow<ResponseResult<Int>> = dataSource.destroy(customer)
}