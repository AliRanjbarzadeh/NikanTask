package ir.aliranjbarzadeh.nikantask.domain.repositories

import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult

interface CustomerRepository {
	suspend fun list(): ResponseResult<List<Customer>>
	suspend fun store(customer: Customer): ResponseResult<Long>
	suspend fun update(customer: Customer): ResponseResult<Int>
	suspend fun destroy(customer: Customer): ResponseResult<Int>
}