package ir.aliranjbarzadeh.nikantask.presentation.orders.edit.customer

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.interfaces.ListViewModel
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.customer.CustomerListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class OrderEditCustomerViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val customerListUseCase: CustomerListUseCase,
) : BaseViewModel(dispatchersProvider), ListViewModel<Customer> {
	@Inject
	lateinit var logger: Logger

	lateinit var orderSummery: OrderSummery

	override val limit: Int = 30
	override var offset: Int = 0
	override var allItemsLoaded: Boolean = false

	override val _items = MutableStateFlow<ResponseResult.Success<List<Customer>>?>(null)
	override val items: StateFlow<ResponseResult.Success<List<Customer>>?> = _items.asStateFlow()

	override val _isEmptyList = MutableStateFlow<Boolean?>(null)
	override val isEmptyList: StateFlow<Boolean?> = _isEmptyList.asStateFlow()

	//Check if already fetched customers
	fun fetchCustomersIfNeeded() {
		if (_items.value == null) {
			fetchCustomers()
		} else if (_items.value?.data?.isEmpty() == true) {
			fetchCustomers()
		}
	}

	fun fetchCustomers(isNextPage: Boolean = false) {
		if (allItemsLoaded) return

		if (isNextPage)
			offset += limit

		executeWithLoading {
			customerListUseCase(limit, offset)
				.collectLatest { result ->
					if (result is ResponseResult.Success) {
						setItems(result)
					} else if (result is ResponseResult.Error) {
						if (isNextPage)
							offset -= limit
						setError(result)
					}
				}
		}
	}
}