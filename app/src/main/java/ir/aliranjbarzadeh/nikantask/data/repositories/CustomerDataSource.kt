package ir.aliranjbarzadeh.nikantask.data.repositories

import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.Flow

interface CustomerDataSource {
	fun list(): Flow<ResponseResult<List<Customer>>>
	fun store(customer: Customer): Flow<ResponseResult<Long>>
	fun update(customer: Customer): Flow<ResponseResult<Int>>
	fun destroy(customer: Customer): Flow<ResponseResult<Int>>
}