package ir.aliranjbarzadeh.nikantask.presentation.orders.checkout

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import ir.aliranjbarzadeh.nikantask.core.interfaces.StoreViewModel
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.data.models.Order
import ir.aliranjbarzadeh.nikantask.data.models.OrderProduct
import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.order.OrderStoreUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val orderStoreUseCase: OrderStoreUseCase,
) : BaseViewModel(dispatchersProvider), StoreViewModel<OrderSummery> {

	@Inject
	lateinit var logger: Logger

	override val _store = MutableStateFlow<ResponseResult.Success<OrderSummery>?>(null)
	override val store = _store.asStateFlow()

	fun saveOrder(customer: Customer, products: List<Product>) {
		executeWithLoading {
			orderStoreUseCase(
				order = Order.forDatabase(customer.id, DateTimeHelper.currentDateUTC()),
				customer = customer,
				orderProducts = products.map { product ->
					OrderProduct(
						productId = product.id,
						quantity = product.getQuantityInt()
					)
				}
			).collectLatest { result ->
				if (result is ResponseResult.Success) {
					setItem(result)
				} else if (result is ResponseResult.Error) {
					setError(result)
				}
			}
		}
	}
}