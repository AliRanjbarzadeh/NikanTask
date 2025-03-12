package ir.aliranjbarzadeh.nikantask.data.repositories

import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.CustomerRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepositoryImpl @Inject constructor(private val dataSource: CustomerDataSource) : CustomerRepository {
	override suspend fun list(): ResponseResult<List<Customer>> = dataSource.list()

	override suspend fun store(customer: Customer): ResponseResult<Long> = dataSource.store(customer)

	override suspend fun update(customer: Customer): ResponseResult<Int> = dataSource.update(customer)

	override suspend fun destroy(customer: Customer): ResponseResult<Int> = dataSource.destroy(customer)
}