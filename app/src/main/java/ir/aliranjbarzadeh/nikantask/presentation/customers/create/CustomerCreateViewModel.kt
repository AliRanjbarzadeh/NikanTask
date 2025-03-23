package ir.aliranjbarzadeh.nikantask.presentation.customers.create

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import ir.aliranjbarzadeh.nikantask.core.interfaces.StoreViewModel
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.customer.CustomerStoreUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class CustomerCreateViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val customerStoreUseCase: CustomerStoreUseCase,
) : BaseViewModel(dispatchersProvider), StoreViewModel<Customer> {

	override val _store = MutableStateFlow<ResponseResult.Success<Customer>?>(null)
	override val store: StateFlow<ResponseResult.Success<Customer>?> = _store.asStateFlow()

	@Inject
	lateinit var logger: Logger

	val fullName = MutableLiveData<String>("")
	val mobile = MutableLiveData<String>("")

	fun store() {
		val customer = Customer.forDatabase(fullName.value!!, mobile.value!!, DateTimeHelper.currentDateUTC())
		logger.debug(customer, "STORE_TAG")
		executeWithLoading {
			customerStoreUseCase(customer)
				.collectLatest { result ->
					logger.debug(result, "STORE_TAG")
					if (result is ResponseResult.Success) {
						setItem(result)
					} else if (result is ResponseResult.Error) {
						setError(result)
					}
				}
		}
	}
}