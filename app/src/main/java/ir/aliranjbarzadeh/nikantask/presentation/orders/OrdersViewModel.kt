package ir.aliranjbarzadeh.nikantask.presentation.orders

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.interfaces.DestroyViewModel
import ir.aliranjbarzadeh.nikantask.core.interfaces.ListViewModel
import ir.aliranjbarzadeh.nikantask.core.interfaces.SeedViewModel
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Order
import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.SeedUseCase
import ir.aliranjbarzadeh.nikantask.domain.usecases.order.OrderDestroyUseCase
import ir.aliranjbarzadeh.nikantask.domain.usecases.order.OrderListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val orderListUseCase: OrderListUseCase,
	private val orderDestroyUseCase: OrderDestroyUseCase,
	private val seedUseCase: SeedUseCase,
) : BaseViewModel(dispatchersProvider), ListViewModel<OrderSummery>, DestroyViewModel, SeedViewModel {

	@Inject
	lateinit var logger: Logger

	override val _seed = MutableStateFlow<ResponseResult.Success<Boolean>?>(null)
	override val seed = _seed.asStateFlow()

	override val limit: Int = 30
	override var offset: Int = 0
	override var allItemsLoaded: Boolean = false

	override val _items = MutableStateFlow<ResponseResult.Success<List<OrderSummery>>?>(null)
	override val items: StateFlow<ResponseResult.Success<List<OrderSummery>>?> = _items.asStateFlow()

	override val _isEmptyList = MutableStateFlow<Boolean?>(null)
	override val isEmptyList: StateFlow<Boolean?> = _isEmptyList.asStateFlow()

	override val _destroy = MutableStateFlow<Int>(-1)
	override val destroy: StateFlow<Int> = _destroy.asStateFlow()

	//Check if already fetched orders
	fun fetchOrdersIfNeeded() {
		if (_items.value == null) {
			fetchOrders()
		} else if (_items.value?.data?.isEmpty() == true) {
			fetchOrders()
		}
	}

	fun fetchOrders(isNextPage: Boolean = false) {
		if (allItemsLoaded) return

		if (isNextPage)
			offset += limit

		executeWithLoading(isNextPage) {
			orderListUseCase(limit, offset)
				.collectLatest { result ->
					if (result is ResponseResult.Success) {
						setItems(result, isNextPage)
					} else if (result is ResponseResult.Error) {
						if (isNextPage)
							offset -= limit
						setError(result)
					}
				}
		}
	}

	fun destroyOrder(order: Order, position: Int) {
		executeWithLoading {
			orderDestroyUseCase(order)
				.collectLatest { result ->
					if (result is ResponseResult.Success) {
						destroyed(position)
					} else if (result is ResponseResult.Error) {
						setError(result)
					}
				}
		}
	}

	fun listCleared() {
		//Reset offset
		offset = 0

		//Reset items & empty list
		_items.value = null
		_isEmptyList.value = true
	}

	fun seed() {
		execute {
			seedUseCase()
				.collectLatest { result ->
					if (result is ResponseResult.Success) {
						_seed.value = result
					} else if (result is ResponseResult.Error) {
						setError(result)
					}
				}
		}
	}
}