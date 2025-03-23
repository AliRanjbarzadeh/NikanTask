package ir.aliranjbarzadeh.nikantask.data.repositories.local

import ir.aliranjbarzadeh.nikantask.core.exceptions.LocalExceptionHandler
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.data.repositories.ProductDataSource
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.ProductDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.ProductModel
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.StatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductDataSource @Inject constructor(
	private val dao: ProductDao,
	private val localExceptionHandler: LocalExceptionHandler,
) : ProductDataSource {
	override fun list(limit: Int, offset: Int): Flow<ResponseResult<List<Product>>> = flow {
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

	override fun store(product: Product): Flow<ResponseResult<Product>> = flow {
		try {
			product.id = dao.store(ProductModel.fromModel(product))
			emit(ResponseResult.Success(product))
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

	override fun update(product: Product): Flow<ResponseResult<Product>> = flow {
		try {
			dao.update(ProductModel.fromModel(product))
			emit(ResponseResult.Success(product))
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

	override fun destroy(product: Product): Flow<ResponseResult<Boolean>> = flow {
		try {
			emit(ResponseResult.Success(dao.destroy(product.id) > 0))
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