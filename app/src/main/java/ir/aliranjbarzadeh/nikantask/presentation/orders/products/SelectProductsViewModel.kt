package ir.aliranjbarzadeh.nikantask.presentation.orders.products

import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.interfaces.ListViewModel
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.product.ProductListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class SelectProductsViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val productListUseCase: ProductListUseCase,
) : BaseViewModel(dispatchersProvider), ListViewModel<Product> {

	@Inject
	lateinit var logger: Logger

	override val limit: Int = 30
	override var offset: Int = 0
	override var allItemsLoaded: Boolean = false

	override val _items = MutableStateFlow<ResponseResult.Success<List<Product>>?>(null)
	override val items: StateFlow<ResponseResult.Success<List<Product>>?> = _items.asStateFlow()

	override val _isEmptyList = MutableStateFlow<Boolean?>(null)
	override val isEmptyList: StateFlow<Boolean?> = _isEmptyList.asStateFlow()

	//Check if already fetched products
	fun fetchProductsIfNeeded() {
		if (_items.value == null) {
			fetchProducts()
		} else if (_items.value?.data?.isEmpty() == true) {
			fetchProducts()
		}
	}

	fun fetchProducts(isNextPage: Boolean = false) {
		if (allItemsLoaded) return

		if (isNextPage)
			offset += limit

		executeWithLoading(isNextPage) {
			productListUseCase(limit, offset)
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
}