package ir.aliranjbarzadeh.nikantask.data.repositories

import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.data.models.Order
import ir.aliranjbarzadeh.nikantask.data.models.OrderProduct
import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(private val dataSource: OrderDataSource) : OrderRepository {
	override fun list(limit: Int, offset: Int): Flow<ResponseResult<List<OrderSummery>>> = dataSource.list(limit, offset)

	override fun storeOrderWithProducts(order: Order, customer: Customer, orderProducts: List<OrderProduct>): Flow<ResponseResult<OrderSummery>> = dataSource.storeOrderWithProducts(order, customer, orderProducts)

	override fun updateOrderWithProducts(order: Order, customer: Customer, orderProducts: List<OrderProduct>): Flow<ResponseResult<OrderSummery>> = dataSource.updateOrderWithProducts(order, customer, orderProducts)

	override fun orderProducts(orderId: Long): Flow<ResponseResult<List<Product>>> = dataSource.orderProducts(orderId)

	override fun destroy(order: Order): Flow<ResponseResult<Boolean>> = dataSource.destroy(order)
}