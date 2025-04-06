package ir.aliranjbarzadeh.nikantask.domain.usecases.order

import ir.aliranjbarzadeh.nikantask.data.models.Order
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderDestroyUseCase @Inject constructor(private val repository: OrderRepository) {
	operator fun invoke(order: Order): Flow<ResponseResult<Boolean>> = repository.destroy(order)
}