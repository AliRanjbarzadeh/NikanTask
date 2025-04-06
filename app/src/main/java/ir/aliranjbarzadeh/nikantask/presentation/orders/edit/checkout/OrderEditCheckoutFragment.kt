package ir.aliranjbarzadeh.nikantask.presentation.orders.edit.checkout

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.core.FragmentResults
import ir.aliranjbarzadeh.nikantask.core.extensions.observe
import ir.aliranjbarzadeh.nikantask.core.helpers.PhoneHelper
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.databinding.FragmentCheckoutBinding
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.presentation.libs.ShopCartDeleteDialog
import ir.aliranjbarzadeh.nikantask.presentation.libs.ShopCartDialog
import ir.aliranjbarzadeh.nikantask.presentation.orders.products.SelectProductsAdapter
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnCartActions
import javax.inject.Inject

@AndroidEntryPoint
class OrderEditCheckoutFragment : BaseFragment<FragmentCheckoutBinding>(
	layoutResId = R.layout.fragment_checkout,
	titleResId = R.string.checkout,
	isShowBackButton = true
), OnCartActions<Product> {
	@Inject
	lateinit var logger: Logger

	private val viewModel: OrderEditCheckoutViewModel by viewModels()
	private val args: OrderEditCheckoutFragmentArgs by navArgs()

	private val selectProductsAdapter = SelectProductsAdapter().apply {
		onCartActions = this@OrderEditCheckoutFragment
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel.orderSummery = args.orderSummery

		selectProductsAdapter.addItems(args.products.toMutableList())

		setupObservers()
	}

	override fun setTitle() {
		requireActivity().setTitle(getString(R.string.order_edit_checkout, viewModel.orderSummery.order.id.toString()))
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupAdapter()

		binding.customer = args.customer

		binding.btnCall.setOnClickListener {
			PhoneHelper.makeCall(requireContext(), args.customer.mobile)
		}

		binding.btnSaveOrder.also {
			it.setOnClickListener {
				val products = selectProductsAdapter.getShoppingProducts()
				if (products.isEmpty()) {
					MaterialAlertDialogBuilder(requireContext())
						.setTitle(R.string.select_products)
						.setMessage(R.string.select_products_error)
						.setNegativeButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
						.show()
				} else {
					viewModel.updateOrder(args.customer, products)
				}
			}

			it.setText(R.string.update)
			it.setIconResource(R.drawable.ic_refresh)
		}
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
		}
	}

	private fun setupObservers() {
		viewModel.run {
			observe(isLoading, ::initLoading)
			observe(update, ::initUpdate)
			observe(error, ::initError)
		}
	}

	private fun initUpdate(result: ResponseResult.Success<OrderSummery>?) {
		result?.let {
			logger.debug("data is -> ${result.data}", TAG)
			setFragmentResult(FragmentResults.Order.UPDATE, bundleOf("item" to result.data, "position" to args.position))
			findNavController().popBackStack(R.id.ordersFragment, false)
		}
	}

	private fun initError(result: ResponseResult.Error?) {
		result?.let {
			val error = it.data

			MaterialAlertDialogBuilder(requireContext())
				.setTitle(R.string.error)
				.setMessage(error.message)
				.setPositiveButton(R.string.try_again) { dialog, _ ->
					binding.btnSaveOrder.callOnClick()
					dialog.dismiss()
				}
				.setNegativeButton(R.string.close) { dialog, _ ->
					dialog.dismiss()
				}
		}
	}
}