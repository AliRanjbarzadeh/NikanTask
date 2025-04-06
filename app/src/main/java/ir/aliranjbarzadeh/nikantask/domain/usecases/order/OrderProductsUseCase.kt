package ir.aliranjbarzadeh.nikantask.domain.usecases.order

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
class OrderProductsUseCase @Inject constructor(private val repository: OrderRepository) {
	operator fun invoke(orderId: Long): Flow<ResponseResult<List<Product>>> = repository.orderProducts(orderId)
}