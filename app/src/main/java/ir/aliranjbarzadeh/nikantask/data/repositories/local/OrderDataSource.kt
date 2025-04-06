package ir.aliranjbarzadeh.nikantask.data.repositories.local

import ir.aliranjbarzadeh.nikantask.core.exceptions.LocalExceptionHandler
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.data.models.Order
import ir.aliranjbarzadeh.nikantask.data.models.OrderProduct
import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.data.repositories.OrderDataSource
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.OrderDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.OrderModel
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.OrderProductPivot
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.StatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderDataSource @Inject constructor(
	private val dao: OrderDao,
	private val localExceptionHandler: LocalExceptionHandler,
) : OrderDataSource {
	override fun list(limit: Int, offset: Int): Flow<ResponseResult<List<OrderSummery>>> = flow {
		try {
			val items = dao.list(limit, offset)
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

	override fun storeOrderWithProducts(order: Order, customer: Customer, orderProducts: List<OrderProduct>): Flow<ResponseResult<OrderSummery>> = flow {
		try {
			order.id = dao.storeOrderWithProducts(OrderModel.fromModel(order), orderProducts.map { OrderProductPivot.fromModel(it) })

			emit(ResponseResult.Success(OrderSummery(order, customer, orderProducts.size)))
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

	override fun updateOrderWithProducts(order: Order, customer: Customer, orderProducts: List<OrderProduct>): Flow<ResponseResult<OrderSummery>> = flow {
		try {
			order.customerId = customer.id
			dao.updateOrderWithProducts(OrderModel.fromModel(order), orderProducts.map { OrderProductPivot.fromModel(it) })
			emit(ResponseResult.Success(OrderSummery(order, customer, orderProducts.size)))
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

	override fun orderProducts(orderId: Long): Flow<ResponseResult<List<Product>>> = flow {
		try {
			val items = dao.orderProducts(orderId)
			emit(ResponseResult.Success(items.map { it.toDomain() }))
		} catch (e: Exception) {
			emit(
				ResponseResult.Error(
					localExceptionHandler.traceErrorException(
						throwable = e,
						statusCode = StatusCode.RoomOrderProducts
					)
				)
			)
		}
	}

	override fun destroy(order: Order): Flow<ResponseResult<Boolean>> = flow {
		try {
			emit(ResponseResult.Success(dao.destroy(order.id) > 0))
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