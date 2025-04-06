package ir.aliranjbarzadeh.nikantask.domain.repositories

import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.data.models.Order
import ir.aliranjbarzadeh.nikantask.data.models.OrderProduct
import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
	fun list(limit: Int, offset: Int): Flow<ResponseResult<List<OrderSummery>>>
	fun storeOrderWithProducts(order: Order, customer: Customer, orderProducts: List<OrderProduct>): Flow<ResponseResult<OrderSummery>>
	fun updateOrderWithProducts(order: Order, customer: Customer, orderProducts: List<OrderProduct>): Flow<ResponseResult<OrderSummery>>
	fun orderProducts(orderId: Long): Flow<ResponseResult<List<Product>>>
	fun destroy(order: Order): Flow<ResponseResult<Boolean>>
}