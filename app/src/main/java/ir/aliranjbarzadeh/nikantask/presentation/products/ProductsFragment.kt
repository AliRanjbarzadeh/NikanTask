package ir.aliranjbarzadeh.nikantask.presentation.products

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.core.FragmentResults
import ir.aliranjbarzadeh.nikantask.core.extensions.navTo
import ir.aliranjbarzadeh.nikantask.core.extensions.observe
import ir.aliranjbarzadeh.nikantask.core.extensions.reachEnd
import ir.aliranjbarzadeh.nikantask.core.extensions.repeatObserve
import ir.aliranjbarzadeh.nikantask.core.interfaces.FragmentResultListener
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.databinding.FragmentProductsBinding
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.presentation.libs.ActionDialog
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnActionClick
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnItemClickListener
import javax.inject.Inject

@AndroidEntryPoint
class ProductsFragment : BaseFragment<FragmentProductsBinding>(
	layoutResId = R.layout.fragment_products,
	titleResId = R.string.products,
	isShowBackButton = false
), OnItemClickListener<Product>, FragmentResultListener, OnActionClick<Product> {
	@Inject
	lateinit var logger: Logger

	private val productsAdapter = ProductsAdapter().apply {
		onItemClickListener = this@ProductsFragment
	}

	private val viewModel: ProductsViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setFragmentResultListener(FragmentResults.Product.STORE, ::initFragmentResultListener)
		setFragmentResultListener(FragmentResults.Product.UPDATE, ::initFragmentResultListener)

		setupObservers()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupAdapter()

		binding.fabAddProduct.setOnClickListener {
			navTo(ProductsFragmentDirections.toProductCreate())
		}

		viewModel.fetchProductsIfNeeded()
	}

	override fun getMainView(): ViewGroup? {
		return binding.main
	}

	override fun onItemClick(item: Product, position: Int) {
		ActionDialog<Product>(requireContext(), item, position, this)
			.show()
	}

	override fun onEdit(item: Product, position: Int) {
		navTo(ProductsFragmentDirections.toProductEdit(item, position))
	}

	override fun onDelete(item: Product, position: Int) {
		MaterialAlertDialogBuilder(requireContext())
			.setTitle(R.string.delete)
			.setMessage(getString(R.string.sure_delete, item.name))
			.setPositiveButton(R.string.yes) { dialog, _ ->
				dialog.dismiss()
				viewModel.destroyProduct(item, position)
			}
			.setNegativeButton(R.string.close) { dialog, _ ->
				dialog.dismiss()
			}
			.show()
	}

	override fun initFragmentResultListener(key: String, bundle: Bundle) {
		if (key.equals(FragmentResults.Product.STORE)) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				bundle.getParcelable("item", Product::class.java)
			} else {
				@Suppress("DEPRECATION")
				bundle.getParcelable("item")
			}?.let { product ->
				if (productsAdapter.addItem(product)) {
					toggleEmptyList(false)
				}
			}
		} else if (key.equals(FragmentResults.Product.UPDATE)) {
			val position = bundle.getInt("position")

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				bundle.getParcelable("item", Product::class.java)
			} else {
				@Suppress("DEPRECATION")
				bundle.getParcelable("item")
			}?.let { product ->
				productsAdapter.updateItem(position, product)
			}
		}
	}

	private fun setupAdapter() {
		binding.rvProducts.apply {
			layoutManager = LinearLayoutManager(requireContext())
			setHasFixedSize(true)
			adapter = productsAdapter
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
			observe(destroy, ::initDestroy)
		}
	}

	private fun initProducts(result: ResponseResult.Success<List<Product>>?) {
		result?.let {
			logger.debug("data is -> ${result.data}", TAG)
			productsAdapter.submitList(result.data)
		}
	}

	private fun initDestroy(position: Int) {
		if (position >= 0) {
			if (productsAdapter.removeItem(position)) {
				viewModel.listCleared()
			}
		}
	}

	private fun initError(result: ResponseResult.Error?) {
		result?.let {
			logger.error("error is -> ${result.data.message}", TAG)
			logger.error("error is -> ${result.data.statusCode}", TAG)
		}
	}
}