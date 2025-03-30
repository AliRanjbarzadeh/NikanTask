package ir.aliranjbarzadeh.nikantask.domain.usecases.order

import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.OrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderListUseCase @Inject constructor(private val repository: OrderRepository) {
	operator fun invoke(limit: Int, offset: Int): Flow<ResponseResult<List<OrderSummery>>> = repository.list(limit, offset)
}