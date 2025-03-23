package ir.aliranjbarzadeh.nikantask.presentation.customers.edit

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.interfaces.UpdateViewModel
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.customer.CustomerUpdateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class CustomerEditViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val customerUpdateUseCase: CustomerUpdateUseCase,
) : BaseViewModel(dispatchersProvider), UpdateViewModel<Customer> {

	override val _update = MutableStateFlow<ResponseResult.Success<Customer>?>(null)
	override val update: StateFlow<ResponseResult.Success<Customer>?> = _update.asStateFlow()

	@Inject
	lateinit var logger: Logger

	private lateinit var customer: Customer

	val fullName = MutableLiveData<String>("")
	val mobile = MutableLiveData<String>("")

	fun setCustomer(customer: Customer) {
		this.customer = customer
		fullName.value = customer.name
		mobile.value = customer.mobile
	}

	fun update() {
		customer.also {
			it.name = fullName.value!!
			it.mobile = mobile.value!!
		}
		logger.debug(customer, "UPDATE_TAG")
		executeWithLoading {
			customerUpdateUseCase(customer)
				.collectLatest { result ->
					logger.debug(result, "UPDATE_TAG")
					if (result is ResponseResult.Success) {
						updateItem(result)
					} else if (result is ResponseResult.Error) {
						setError(result)
					}
				}
		}
	}
}