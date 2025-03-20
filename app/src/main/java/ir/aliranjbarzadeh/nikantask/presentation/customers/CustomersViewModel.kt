package ir.aliranjbarzadeh.nikantask.presentation.customers

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.interfaces.ListViewModel
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.customer.CustomerListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CustomersViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val customerListUseCase: CustomerListUseCase,
) : BaseViewModel(dispatchersProvider), ListViewModel<Customer> {

	@Inject
	lateinit var logger: Logger

	override val _items = MutableStateFlow<ResponseResult.Success<List<Customer>>?>(null)
	override val items: StateFlow<ResponseResult.Success<List<Customer>>?> = _items.asStateFlow()

	override val _isEmptyList = MutableStateFlow<Boolean?>(null)
	override val isEmptyList: StateFlow<Boolean?> = _isEmptyList.stateIn(
		scope = viewModelScope,
		started = SharingStarted.Eagerly,
		initialValue = null
	)

	//Check if already fetched customers return false to init views
	fun fetchCustomersIfNeeded(): Boolean {
		if (_items.value == null) {
			fetchCustomers()
			return true
		} else if (_items.value?.data?.isEmpty() == true) {
			fetchCustomers()
			return true
		}
		return false
	}

	fun fetchCustomers() {
		executeWithLoading {
			customerListUseCase()
				.collectLatest { result ->
					logger.debug("result is $result", "CustomersLog")
					if (result is ResponseResult.Success) {
						setItems(result)
					} else if (result is ResponseResult.Error) {
						_error.value = result
					}
				}
		}
	}
}