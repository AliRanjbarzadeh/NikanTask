package ir.aliranjbarzadeh.nikantask.data.repositories

import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(private val dataSource: ProductDataSource) : ProductRepository {
	override fun list(limit: Int, offset: Int): Flow<ResponseResult<List<Product>>> = dataSource.list(limit, offset)

	override fun store(product: Product): Flow<ResponseResult<Product>> = dataSource.store(product)

	override fun update(product: Product): Flow<ResponseResult<Product>> = dataSource.update(product)

	override fun destroy(product: Product): Flow<ResponseResult<Boolean>> = dataSource.destroy(product)
}