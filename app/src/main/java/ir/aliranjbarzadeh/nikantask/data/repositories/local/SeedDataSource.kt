package ir.aliranjbarzadeh.nikantask.data.repositories.local

import com.github.javafaker.Faker
import ir.aliranjbarzadeh.nikantask.core.exceptions.LocalExceptionHandler
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import ir.aliranjbarzadeh.nikantask.data.repositories.SeedDataSource
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.CustomerDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.OrderDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.ProductDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.CustomerModel
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.OrderModel
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.OrderProductPivot
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.ProductModel
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.StatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeedDataSource @Inject constructor(
	private val customerDao: CustomerDao,
	private val productDao: ProductDao,
	private val orderDao: OrderDao,
	private val localExceptionHandler: LocalExceptionHandler,
) : SeedDataSource {
	override fun seed(): Flow<ResponseResult<Boolean>> = flow {
		try {
			//Reset orders
			orderDao.truncate()

			//Reset customers
			customerDao.truncate()

			//Reset products
			productDao.truncate()

			val startDate = DateTimeHelper.currentDateUTC(10)
			val date = DateTimeHelper.currentDateUTC()
			val faker = Faker(Locale("fa"))

			val customers = mutableListOf<CustomerModel>()
			val products = mutableListOf<ProductModel>()
			var cDate = faker.date().between(startDate, date)
			for (i in 0..99) {
				cDate = faker.date().between(startDate, date)
				customers.add(
					CustomerModel(
						name = faker.name().fullName(),
						mobile = faker.phoneNumber().phoneNumber(),
						createdAt = cDate,
						updatedAt = cDate
					)
				)

				cDate = faker.date().between(startDate, date)
				products.add(
					ProductModel(
						name = faker.food().fruit(),
						code = faker.number().randomNumber(6, true).toString(),
						createdAt = cDate,
						updatedAt = cDate
					)
				)
			}

			val customerIds = customerDao.storeMany(customers)
			customers.forEachIndexed { index, customer ->
				customer.customerId = customerIds[index]
			}

			val productIds = productDao.storeMany(products)
			products.forEachIndexed { index, product ->
				product.productId = productIds[index]
			}

			for (i in 1..40) {
				val days = when {
					i in (5..10) -> 1
					i in (11..20) -> 2
					i in (21..30) -> 3
					i in (31..40) -> 4
					else -> 0
				}

				cDate = DateTimeHelper.currentDateUTC(4 - days)
				val customerRefId = customers.random().customerId
				val orderModel = OrderModel(
					customerRefId = customerRefId,
					createdAt = cDate,
					updatedAt = cDate
				)

				val pivots = products.shuffled().take((1..20).random()).map {
					OrderProductPivot(
						productRefId = it.productId,
						quantity = (1..10).random()
					)
				}

				orderDao.storeOrderWithProducts(orderModel, pivots)
			}


			emit(ResponseResult.Success(true))
		} catch (e: Exception) {
			emit(
				ResponseResult.Error(
					localExceptionHandler.traceErrorException(
						throwable = e,
						statusCode = StatusCode.RoomSeed
					)
				)
			)
		}
	}
}