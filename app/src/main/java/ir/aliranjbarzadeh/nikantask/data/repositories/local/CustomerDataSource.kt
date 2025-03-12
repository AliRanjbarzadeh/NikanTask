package ir.aliranjbarzadeh.nikantask.data.repositories.local

import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.data.models.Error
import ir.aliranjbarzadeh.nikantask.data.repositories.CustomerDataSource
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.CustomerDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.CustomerModel
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.StatusCode
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerDataSource @Inject constructor(private val dao: CustomerDao) : CustomerDataSource {
	override suspend fun list(): ResponseResult<List<Customer>> {
		try {
			val items = dao.list()
			return ResponseResult.Success(items.map { it.toDomain() })
		} catch (e: Exception) {
			return ResponseResult.Error(
				Error(
					message = e.message,
					statusCode = StatusCode.RoomList
				)
			)
		}
	}

	override suspend fun store(customer: Customer): ResponseResult<Long> {
		try {
			val result = dao.store(CustomerModel.fromModel(customer))
			return ResponseResult.Success(result)
		} catch (e: Exception) {
			return ResponseResult.Error(
				Error(
					message = e.message,
					statusCode = StatusCode.RoomStore
				)
			)
		}
	}

	override suspend fun update(customer: Customer): ResponseResult<Int> {
		try {
			val result = dao.update(CustomerModel.fromModel(customer))
			return ResponseResult.Success(result)
		} catch (e: Exception) {
			return ResponseResult.Error(
				Error(
					message = e.message,
					statusCode = StatusCode.RoomUpdate
				)
			)
		}
	}

	override suspend fun destroy(customer: Customer): ResponseResult<Int> {
		try {
			val result = dao.destroy(customer.id)
			return ResponseResult.Success(result)
		} catch (e: Exception) {
			return ResponseResult.Error(
				Error(
					message = e.message,
					statusCode = StatusCode.RoomDestroy
				)
			)
		}
	}
}