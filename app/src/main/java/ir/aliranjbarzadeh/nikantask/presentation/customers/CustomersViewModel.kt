package ir.aliranjbarzadeh.nikantask.presentation.customers

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.interfaces.ListViewModel
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.customer.CustomerListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CustomersViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val customerListUseCase: CustomerListUseCase,
) : BaseViewModel(dispatchersProvider), ListViewModel<Customer> {

	override val _items = MutableStateFlow<ResponseResult.Success<List<Customer>>?>(null)
	override val items: StateFlow<ResponseResult.Success<List<Customer>>?> = _items.asStateFlow()

	override val _isEmptyList = MutableStateFlow<Boolean>(false)
	override val isEmptyList: StateFlow<Boolean> = _isEmptyList.asStateFlow()

	init {
		fetchCustomers()
	}

	fun fetchCustomers() {
		executeWithLoading {
			customerListUseCase()
				.collect { result ->
					if (result is ResponseResult.Success) {
						setItems(result)
					} else if (result is ResponseResult.Error) {
						_error.value = result
					}
				}
		}
	}
}