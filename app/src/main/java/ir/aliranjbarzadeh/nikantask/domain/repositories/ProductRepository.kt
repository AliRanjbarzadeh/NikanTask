package ir.aliranjbarzadeh.nikantask.domain.repositories

import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
	fun list(limit: Int, offset: Int): Flow<ResponseResult<List<Product>>>
	fun store(product: Product): Flow<ResponseResult<Product>>
	fun update(product: Product): Flow<ResponseResult<Product>>
	fun destroy(product: Product): Flow<ResponseResult<Boolean>>
}