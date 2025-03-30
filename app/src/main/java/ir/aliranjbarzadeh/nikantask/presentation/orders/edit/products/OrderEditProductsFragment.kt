package ir.aliranjbarzadeh.nikantask.presentation.orders.edit.products

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.core.extensions.navTo
import ir.aliranjbarzadeh.nikantask.core.extensions.observe
import ir.aliranjbarzadeh.nikantask.core.extensions.reachEnd
import ir.aliranjbarzadeh.nikantask.core.extensions.repeatObserve
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.databinding.FragmentSelectProductsBinding
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.StatusCode
import ir.aliranjbarzadeh.nikantask.presentation.libs.ShopCartDeleteDialog
import ir.aliranjbarzadeh.nikantask.presentation.libs.ShopCartDialog
import ir.aliranjbarzadeh.nikantask.presentation.orders.products.SelectProductsAdapter
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnCartActions
import javax.inject.Inject

@AndroidEntryPoint
class OrderEditProductsFragment : BaseFragment<FragmentSelectProductsBinding>(
	layoutResId = R.layout.fragment_select_products,
	titleResId = R.string.order_edit_customer,
	isShowBackButton = true
), OnCartActions<Product> {
	@Inject
	lateinit var logger: Logger

	private val selectProductsAdapter = SelectProductsAdapter().apply {
		onCartActions = this@OrderEditProductsFragment
	}

	private val viewModel: OrderEditProductsViewModel by viewModels()
	private val args: OrderEditProductsFragmentArgs by navArgs()

	private var orderProducts = mutableListOf<Product>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel.orderSummery = args.orderSummery

		setupObservers()
	}

	override fun setTitle() {
		requireActivity().setTitle(getString(R.string.order_edit_products, viewModel.orderSummery.order.id.toString()))
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupAdapter()

		if (viewModel.fetchOrderProducts()) {
			viewModel.fetchProductsIfNeeded()
		}

		binding.btnContinue.setOnClickListener {
			val products = selectProductsAdapter.getShoppingProducts()
			if (products.isEmpty()) {
				MaterialAlertDialogBuilder(requireContext())
					.setTitle(R.string.select_products)
					.setMessage(R.string.select_products_error)
					.setNegativeButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
					.show()
			} else {
				navTo(OrderEditProductsFragmentDirections.toEditCheckout(viewModel.orderSummery, args.customer, products.toTypedArray(), args.position))
			}
		}
	}

	override fun getMainView(): ViewGroup? {
		return binding.main
	}

	override fun onAddToCart(item: Product, position: Int) {
		ShopCartDialog(requireContext(), item.getQuantityInt()) { quantity ->
			item.setNewQuantity(quantity)
			selectProductsAdapter.updateItem(position, item)
		}.show()
	}

	override fun onEditCartItem(item: Product, position: Int) {
		ShopCartDialog(requireContext(), item.getQuantityInt()) { quantity ->
			item.setNewQuantity(quantity)
			selectProductsAdapter.updateItem(position, item)
		}.show()
	}

	override fun onDeleteFromCart(item: Product, position: Int) {
		ShopCartDeleteDialog(requireContext()) {
			item.setNewQuantity(0)
			selectProductsAdapter.updateItem(position, item)
		}.show()
	}

	private fun setupAdapter() {
		binding.rvProducts.apply {
			layoutManager = GridLayoutManager(requireContext(), 2)
			setHasFixedSize(true)
			adapter = selectProductsAdapter
			reachEnd {
				viewModel.fetchProducts(isNextPage = true)
			}
		}
	}


	private fun setupObservers() {
		viewModel.run {
			observe(isLoading, ::initLoading)
			repeatObserve(isEmptyList, ::initEmptyList)
			observe(orderProducts, ::initOrderProducts)
			observe(items, ::initProducts)
			observe(error, ::initError)
		}
	}

	private fun initOrderProducts(result: ResponseResult.Success<List<Product>>?) {
		result?.let {
			orderProducts = result.data.toMutableList()
			viewModel.fetchProductsIfNeeded()
		}
	}

	private fun initProducts(result: ResponseResult.Success<List<Product>>?) {
		result?.let {
			logger.debug("data is -> ${result.data}", TAG)
			result.data.forEach { product ->
				orderProducts.find { it.id == product.id }?.let {
					product.setNewQuantity(it.getQuantityInt())
				}
			}
			selectProductsAdapter.addItems(result.data)
		}
	}

	private fun initError(result: ResponseResult.Error?) {
		result?.let {
			val error = result.data
			logger.error("error is -> ${error.message}", TAG)
			logger.error("error is -> ${error.statusCode}", TAG)

			MaterialAlertDialogBuilder(requireContext())
				.setTitle(R.string.error)
				.setMessage(error.message)
				.setPositiveButton(R.string.try_again) { dialog, _ ->
					if (error.statusCode == StatusCode.RoomOrderProducts) {
						viewModel.fetchOrderProducts()
					} else if (error.statusCode == StatusCode.RoomList) {
						if (viewModel.offset == 0) {
							viewModel.fetchProductsIfNeeded()
						} else {
							viewModel.fetchProducts(true)
						}
					}
					dialog.dismiss()
				}
				.setNegativeButton(R.string.close) { dialog, _ ->
					dialog.dismiss()
				}
		}
	}
}