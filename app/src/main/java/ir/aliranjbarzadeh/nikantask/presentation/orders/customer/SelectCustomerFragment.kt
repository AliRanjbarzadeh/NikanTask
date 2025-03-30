package ir.aliranjbarzadeh.nikantask.presentation.orders.customer

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.core.extensions.navTo
import ir.aliranjbarzadeh.nikantask.core.extensions.observe
import ir.aliranjbarzadeh.nikantask.core.extensions.reachEnd
import ir.aliranjbarzadeh.nikantask.core.extensions.repeatObserve
import ir.aliranjbarzadeh.nikantask.core.helpers.PhoneHelper
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.databinding.FragmentSelectCustomerBinding
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnCallListener
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnItemClick
import javax.inject.Inject

@AndroidEntryPoint
class SelectCustomerFragment : BaseFragment<FragmentSelectCustomerBinding>(
	layoutResId = R.layout.fragment_select_customer,
	titleResId = R.string.select_customer,
	isShowBackButton = true
), OnItemClick<Customer>, OnCallListener {
	@Inject
	lateinit var logger: Logger

	private val selectCustomerAdapter = SelectCustomerAdapter().apply {
		onItemClick = this@SelectCustomerFragment
		onCallListener = this@SelectCustomerFragment
	}

	private val viewModel: SelectCustomerViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setupObservers()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupAdapter()

		viewModel.fetchCustomersIfNeeded()

		binding.btnContinue.setOnClickListener {
			selectCustomerAdapter.getChecked()?.let { customer ->
				navTo(SelectCustomerFragmentDirections.toSelectProducts(customer))
			} ?: run {
				MaterialAlertDialogBuilder(requireContext())
					.setTitle(R.string.select_customer)
					.setMessage(R.string.select_customer_error)
					.setNegativeButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
					.show()
			}
		}
	}

	override fun getMainView(): ViewGroup? {
		return binding.main
	}

	override fun onItemClick(item: Customer, position: Int) {
		selectCustomerAdapter.changeChecked(position)
	}

	override fun onCall(phone: String) {
		PhoneHelper.makeCall(requireContext(), phone)
	}

	private fun setupAdapter() {
		binding.rvCustomers.apply {
			layoutManager = LinearLayoutManager(requireContext())
			setHasFixedSize(true)
			adapter = selectCustomerAdapter
			reachEnd {
				viewModel.fetchCustomers(isNextPage = true)
			}
		}
	}

	private fun setupObservers() {
		viewModel.run {
			observe(isLoading, ::initLoading)
			repeatObserve(isEmptyList, ::initEmptyList)
			observe(items, ::initCustomers)
			observe(error, ::initError)
		}
	}

	private fun initCustomers(result: ResponseResult.Success<List<Customer>>?) {
		result?.let {
			logger.debug("data is -> ${result.data}", TAG)
			selectCustomerAdapter.addItems(result.data)
		}
	}

	private fun initError(result: ResponseResult.Error?) {
		result?.let {
			logger.error("error is -> ${result.data.message}", TAG)
			logger.error("error is -> ${result.data.statusCode}", TAG)
		}
	}
}