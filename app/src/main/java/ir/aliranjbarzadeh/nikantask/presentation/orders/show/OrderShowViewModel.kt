package ir.aliranjbarzadeh.nikantask.presentation.orders.show

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.interfaces.DetailViewModel
import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.order.OrderProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class OrderShowViewModel @Inject constructor(
	val dispatchersProvider: DispatchersProvider,
	private val orderProductsUseCase: OrderProductsUseCase,
) : BaseViewModel(dispatchersProvider), DetailViewModel<Product> {

	lateinit var orderSummery: OrderSummery

	override val _items = MutableStateFlow<ResponseResult.Success<List<Product>>?>(null)
	override val items: StateFlow<ResponseResult.Success<List<Product>>?> = _items.asStateFlow()

	fun fetchProducts() {
		executeWithLoading {
			orderProductsUseCase(orderSummery.order.id)
				.collectLatest { result ->
					if (result is ResponseResult.Success) {
						setItems(result)
					} else if (result is ResponseResult.Error) {
						setError(result)
					}
				}
		}
	}
}