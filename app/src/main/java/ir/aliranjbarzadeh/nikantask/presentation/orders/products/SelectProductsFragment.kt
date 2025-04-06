package ir.aliranjbarzadeh.nikantask.presentation.orders.products

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
import ir.aliranjbarzadeh.nikantask.presentation.libs.ShopCartDeleteDialog
import ir.aliranjbarzadeh.nikantask.presentation.libs.ShopCartDialog
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnCartActions
import javax.inject.Inject

@AndroidEntryPoint
class SelectProductsFragment : BaseFragment<FragmentSelectProductsBinding>(
	layoutResId = R.layout.fragment_select_products,
	titleResId = R.string.select_products,
	isShowBackButton = true
), OnCartActions<Product> {
	@Inject
	lateinit var logger: Logger

	private val selectProductsAdapter = SelectProductsAdapter().apply {
		onCartActions = this@SelectProductsFragment
	}

	private val viewModel: SelectProductsViewModel by viewModels()
	private val args: SelectProductsFragmentArgs by navArgs()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setupObservers()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupAdapter()

		viewModel.fetchProductsIfNeeded()

		binding.btnContinue.setOnClickListener {
			val products = selectProductsAdapter.getShoppingProducts()
			if (products.isEmpty()) {
				MaterialAlertDialogBuilder(requireContext())
					.setTitle(R.string.select_products)
					.setMessage(R.string.select_products_error)
					.setNegativeButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
					.show()
			} else {
				navTo(SelectProductsFragmentDirections.toCheckout(args.customer, products.toTypedArray()))
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
			observe(items, ::initProducts)
			observe(error, ::initError)
		}
	}

	private fun initProducts(result: ResponseResult.Success<List<Product>>?) {
		result?.let {
			logger.debug("data is -> ${result.data}", TAG)
			selectProductsAdapter.addItems(result.data)
		}
	}

	private fun initError(result: ResponseResult.Error?) {
		result?.let {
			logger.error("error is -> ${result.data.message}", TAG)
			logger.error("error is -> ${result.data.statusCode}", TAG)
		}
	}
}