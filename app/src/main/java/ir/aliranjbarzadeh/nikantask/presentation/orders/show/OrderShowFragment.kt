package ir.aliranjbarzadeh.nikantask.presentation.orders.show

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.core.extensions.observe
import ir.aliranjbarzadeh.nikantask.core.helpers.PhoneHelper
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.databinding.FragmentOrderShowBinding
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.StatusCode
import javax.inject.Inject

@AndroidEntryPoint
class OrderShowFragment : BaseFragment<FragmentOrderShowBinding>(
	layoutResId = R.layout.fragment_order_show,
	titleResId = R.string.show_order,
	isShowBackButton = true
) {
	@Inject
	lateinit var logger: Logger

	private val args: OrderShowFragmentArgs by navArgs()
	private val viewModel: OrderShowViewModel by viewModels()

	private val orderProductsAdapter = OrderProductsAdapter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel.orderSummery = args.orderSummery

		setupObservers()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.orderSummery = viewModel.orderSummery

		setupAdapter()

		viewModel.fetchProducts()

		binding.btnCall.setOnClickListener { PhoneHelper.makeCall(requireContext(), viewModel.orderSummery.customer.mobile) }
	}

	override fun getMainView(): ViewGroup? {
		return binding.main
	}

	private fun setupAdapter() {
		binding.rvProducts.apply {
			layoutManager = LinearLayoutManager(requireContext())
			setHasFixedSize(true)
			adapter = orderProductsAdapter
		}
	}

	private fun setupObservers() {
		viewModel.run {
			observe(isLoading, ::initLoading)
			observe(items, ::initProducts)
			observe(error, ::initError)
		}
	}

	private fun initProducts(result: ResponseResult.Success<List<Product>>?) {
		result?.let {
			logger.debug("data is -> ${result.data}", TAG)
			orderProductsAdapter.addItems(result.data)
		}
	}

	private fun initError(result: ResponseResult.Error?) {
		result?.let {
			val error = it.data
			logger.error("error is -> ${error.message}", TAG)
			logger.error("error is -> ${error.statusCode}", TAG)

			if (error.statusCode == StatusCode.RoomOrderProducts) {
				MaterialAlertDialogBuilder(requireContext())
					.setTitle(R.string.error)
					.setMessage(error.message)
					.setPositiveButton(R.string.try_again) { dialog, _ ->
						viewModel.fetchProducts()
						dialog.dismiss()
					}
					.setNegativeButton(R.string.close) { dialog, _ ->
						dialog.dismiss()
					}
			}
		}
	}
}