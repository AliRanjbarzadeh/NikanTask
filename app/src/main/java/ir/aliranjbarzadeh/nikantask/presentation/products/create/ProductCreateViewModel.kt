package ir.aliranjbarzadeh.nikantask.presentation.products.create

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import ir.aliranjbarzadeh.nikantask.core.interfaces.StoreViewModel
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.product.ProductStoreUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class ProductCreateViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val productStoreUseCase: ProductStoreUseCase,
) : BaseViewModel(dispatchersProvider), StoreViewModel<Product> {

	override val _store = MutableStateFlow<ResponseResult.Success<Product>?>(null)
	override val store: StateFlow<ResponseResult.Success<Product>?> = _store.asStateFlow()

	@Inject
	lateinit var logger: Logger

	val name = MutableLiveData<String>("")
	val code = MutableLiveData<String>("")

	fun store() {
		val product = Product.forDatabase(name.value!!, code.value!!, DateTimeHelper.currentDateUTC())
		logger.debug(product, "STORE_TAG")
		executeWithLoading {
			productStoreUseCase(product)
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