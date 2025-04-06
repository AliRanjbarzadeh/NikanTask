package ir.aliranjbarzadeh.nikantask.presentation.orders

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.AppConfig
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.core.FragmentResults
import ir.aliranjbarzadeh.nikantask.core.extensions.loadFromSp
import ir.aliranjbarzadeh.nikantask.core.extensions.navTo
import ir.aliranjbarzadeh.nikantask.core.extensions.observe
import ir.aliranjbarzadeh.nikantask.core.extensions.reachEnd
import ir.aliranjbarzadeh.nikantask.core.extensions.repeatObserve
import ir.aliranjbarzadeh.nikantask.core.extensions.saveToSp
import ir.aliranjbarzadeh.nikantask.core.interfaces.FragmentResultListener
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery
import ir.aliranjbarzadeh.nikantask.databinding.FragmentOrdersBinding
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.StatusCode
import ir.aliranjbarzadeh.nikantask.presentation.libs.ActionDialog
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnActionClick
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnShowClick
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnToolsClick
import javax.inject.Inject

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding>(
	layoutResId = R.layout.fragment_orders,
	titleResId = R.string.orders,
	isShowBackButton = false
), OnToolsClick<OrderSummery>, FragmentResultListener, OnActionClick<OrderSummery>, OnShowClick<OrderSummery> {
	@Inject
	lateinit var logger: Logger

	private val viewModel: OrdersViewModel by viewModels()

	private val ordersAdapter = OrdersAdapter().apply {
		onToolsClick = this@OrdersFragment
		onShowClick = this@OrdersFragment
	}

	private var seedDialog: AlertDialog? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setFragmentResultListener(FragmentResults.Order.STORE, ::initFragmentResultListener)
		setFragmentResultListener(FragmentResults.Order.UPDATE, ::initFragmentResultListener)

		setupObservers()

		val isSeeded = loadFromSp(AppConfig.SessionKeys.SEEDER, false)
		if (!isSeeded) {
			MaterialAlertDialogBuilder(requireContext())
				.setCancelable(false)
				.setTitle(R.string.seed)
				.setMessage(R.string.seed_description)
				.setPositiveButton(R.string.yes) { dialog, _ ->
					dialog.dismiss()
					startSeeder()
				}
				.setNegativeButton(R.string.close) { dialog, _ ->
					dialog.dismiss()
				}
				.setOnDismissListener {
					saveToSp(AppConfig.SessionKeys.SEEDER, true)
				}
				.show()
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupAdapter()

		viewModel.fetchOrdersIfNeeded()

		binding.fabAddOrder.setOnClickListener {
			navTo(OrdersFragmentDirections.toSelectCustomer())
		}
	}

	override fun getMainView(): ViewGroup? {
		return binding.main
	}

	override fun onToolsClick(item: OrderSummery, position: Int) {
		ActionDialog<OrderSummery>(requireContext(), item, position, this)
			.show()
	}

	override fun onShow(item: OrderSummery) {
		navTo(OrdersFragmentDirections.toOrderShow(item))
	}

	override fun onEdit(item: OrderSummery, position: Int) {
		navTo(OrdersFragmentDirections.toEditCustomer(item, position))
	}

	override fun onDelete(item: OrderSummery, position: Int) {
		MaterialAlertDialogBuilder(requireContext())
			.setTitle(R.string.delete)
			.setMessage(getString(R.string.sure_delete_order, item.order.id.toString()))
			.setPositiveButton(R.string.yes) { dialog, _ ->
				dialog.dismiss()
				viewModel.destroyOrder(item.order, position)
			}
			.setNegativeButton(R.string.close) { dialog, _ ->
				dialog.dismiss()
			}
			.show()
	}

	override fun initFragmentResultListener(key: String, bundle: Bundle) {
		if (key == FragmentResults.Order.STORE) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				bundle.getParcelable("item", OrderSummery::class.java)
			} else {
				@Suppress("DEPRECATION")
				bundle.getParcelable("item")
			}?.let { orderSummery ->
				ordersAdapter.addItem(orderSummery)
				binding.rvOrders.postDelayed({
					binding.rvOrders.smoothScrollToPosition(0)
				}, 200)
			}
		} else if (key == FragmentResults.Order.UPDATE) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				Pair(
					bundle.getParcelable("item", OrderSummery::class.java),
					bundle.getInt("position")
				)
			} else {
				Pair(
					@Suppress("DEPRECATION")
					bundle.getParcelable<OrderSummery>("item"),
					bundle.getInt("position")
				)
			}.let { (orderSummery, position) ->
				orderSummery?.let {
					binding.rvOrders.postDelayed({
						ordersAdapter.updateItem(position, orderSummery)
					}, 200)
				}
			}
		}
	}

	private fun setupAdapter() {
		binding.rvOrders.apply {
			layoutManager = LinearLayoutManager(requireContext())
			setHasFixedSize(true)
			adapter = ordersAdapter
			reachEnd {
				viewModel.fetchOrders(isNextPage = true)
			}
		}
	}

	private fun setupObservers() {
		viewModel.run {
			observe(isLoading, ::initLoading)
			repeatObserve(isEmptyList, ::initEmptyList)
			observe(items, ::initOrders)
			observe(error, ::initError)
			observe(destroy, ::initDestroy)
			observe(seed, ::initSeed)
		}
	}

	private fun initOrders(result: ResponseResult.Success<List<OrderSummery>>?) {
		result?.let {
			logger.debug("data is -> ${result.data}", TAG)
			ordersAdapter.addItems(result.data)
		}
	}

	private fun initDestroy(position: Int) {
		if (position >= 0) {
			if (ordersAdapter.removeItem(position)) {
				viewModel.listCleared()
			}
		}
	}

	private fun startSeeder() {
		viewModel.seed()
		seedDialog = MaterialAlertDialogBuilder(requireContext())
			.setCancelable(false)
			.setTitle(R.string.seed)
			.setMessage(R.string.seeding)
			.show()
	}

	private fun initSeed(result: ResponseResult.Success<Boolean>?) {
		result?.let {
			saveToSp(AppConfig.SessionKeys.SEEDER, true)
			seedDialog?.dismiss()
			ordersAdapter.clearItems()
			viewModel.listCleared()
			viewModel.fetchOrdersIfNeeded()
		}
	}

	private fun initError(result: ResponseResult.Error?) {
		result?.let {
			val error = it.data
			logger.error("error is -> ${error.message}", TAG)
			logger.error("error is -> ${error.statusCode}", TAG)

			//Handle seed error
			if (error.statusCode === StatusCode.RoomSeed) {
				seedDialog?.dismiss()
				logger.error("error trace is -> ${error.data}", TAG)
			} else if (error.statusCode == StatusCode.RoomList) {
				MaterialAlertDialogBuilder(requireContext())
					.setTitle(R.string.error)
					.setMessage(error.message)
					.setPositiveButton(R.string.try_again) { dialog, _ ->
						viewModel.fetchOrdersIfNeeded()
						dialog.dismiss()
					}
					.setNegativeButton(R.string.close) { dialog, _ ->
						dialog.dismiss()
					}
			} else if (error.statusCode == StatusCode.RoomDestroy) {
				Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
			}
		}
	}
}