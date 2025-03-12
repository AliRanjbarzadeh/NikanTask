package ir.aliranjbarzadeh.nikantask.data.repositories

import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult

interface CustomerDataSource {
	suspend fun list(): ResponseResult<List<Customer>>
	suspend fun store(customer: Customer): ResponseResult<Long>
	suspend fun update(customer: Customer): ResponseResult<Int>
	suspend fun destroy(customer: Customer): ResponseResult<Int>
}