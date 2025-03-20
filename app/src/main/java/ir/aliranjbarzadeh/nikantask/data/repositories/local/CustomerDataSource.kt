package ir.aliranjbarzadeh.nikantask.data.repositories.local

import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.exceptions.LocalExceptionHandler
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.data.repositories.CustomerDataSource
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.CustomerDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.CustomerModel
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.StatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerDataSource @Inject constructor(
	private val dao: CustomerDao,
	private val localExceptionHandler: LocalExceptionHandler,
	private val dispatchersProvider: DispatchersProvider,
) : CustomerDataSource {
	override fun list(): Flow<ResponseResult<List<Customer>>> = flow {
		try {
			val items = dao.list()
			emit(ResponseResult.Success(items.map { it.toDomain() }))
		} catch (e: Exception) {
			emit(
				ResponseResult.Error(
					localExceptionHandler.traceErrorException(
						throwable = e,
						statusCode = StatusCode.RoomList
					)
				)
			)
		}
	}

	override fun store(customer: Customer): Flow<ResponseResult<Long>> = flow {
		try {
			val result = dao.store(CustomerModel.fromModel(customer))
			emit(ResponseResult.Success(result))
		} catch (e: Exception) {
			emit(
				ResponseResult.Error(
					localExceptionHandler.traceErrorException(
						throwable = e,
						statusCode = StatusCode.RoomStore
					)
				)
			)
		}
	}

	override fun update(customer: Customer): Flow<ResponseResult<Int>> = flow {
		try {
			val result = dao.update(CustomerModel.fromModel(customer))
			emit(ResponseResult.Success(result))
		} catch (e: Exception) {
			emit(
				ResponseResult.Error(
					localExceptionHandler.traceErrorException(
						throwable = e,
						statusCode = StatusCode.RoomUpdate
					)
				)
			)
		}
	}

	override fun destroy(customer: Customer): Flow<ResponseResult<Int>> = flow {
		try {
			val result = dao.destroy(customer.id)
			emit(ResponseResult.Success(result))
		} catch (e: Exception) {
			emit(
				ResponseResult.Error(
					localExceptionHandler.traceErrorException(
						throwable = e,
						statusCode = StatusCode.RoomDestroy
					)
				)
			)
		}
	}
}