package ir.aliranjbarzadeh.nikantask.presentation.orders.edit.checkout

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.interfaces.UpdateViewModel
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.data.models.OrderProduct
import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.order.OrderUpdateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class OrderEditCheckoutViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val orderUpdateUseCase: OrderUpdateUseCase,
) : BaseViewModel(dispatchersProvider), UpdateViewModel<OrderSummery> {

	@Inject
	lateinit var logger: Logger

	lateinit var orderSummery: OrderSummery

	override val _update = MutableStateFlow<ResponseResult.Success<OrderSummery>?>(null)
	override val update = _update.asStateFlow()

	fun updateOrder(customer: Customer, products: List<Product>) {
		executeWithLoading {
			orderUpdateUseCase(
				order = orderSummery.order,
				customer = customer,
				orderProducts = products.map { product ->
					OrderProduct(
						productId = product.id,
						quantity = product.getQuantityInt()
					)
				}
			).collectLatest { result ->
				if (result is ResponseResult.Success) {
					updateItem(result)
				} else if (result is ResponseResult.Error) {
					setError(result)
				}
			}
		}
	}
}