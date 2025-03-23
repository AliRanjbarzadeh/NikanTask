package ir.aliranjbarzadeh.nikantask.presentation.products.edit

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.interfaces.UpdateViewModel
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.product.ProductUpdateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class ProductEditViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val productUpdateUseCase: ProductUpdateUseCase,
) : BaseViewModel(dispatchersProvider), UpdateViewModel<Product> {

	override val _update = MutableStateFlow<ResponseResult.Success<Product>?>(null)
	override val update: StateFlow<ResponseResult.Success<Product>?> = _update.asStateFlow()

	@Inject
	lateinit var logger: Logger

	private lateinit var product: Product

	val name = MutableLiveData<String>("")
	val code = MutableLiveData<String>("")

	fun setProduct(product: Product) {
		this.product = product
		name.value = product.name
		code.value = product.code
	}

	fun update() {
		product.also {
			it.name = name.value!!
			it.code = code.value!!
		}
		logger.debug(product, "UPDATE_TAG")
		executeWithLoading {
			productUpdateUseCase(product)
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