package ir.aliranjbarzadeh.nikantask.domain.usecases.product

import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductStoreUseCase @Inject constructor(private val repository: ProductRepository) {
	operator fun invoke(product: Product): Flow<ResponseResult<Product>> = repository.store(product)
}