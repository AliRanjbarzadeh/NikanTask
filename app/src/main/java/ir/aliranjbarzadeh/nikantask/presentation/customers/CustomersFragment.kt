package ir.aliranjbarzadeh.nikantask.presentation.customers

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
import ir.aliranjbarzadeh.nikantask.core.helpers.PhoneHelper
import ir.aliranjbarzadeh.nikantask.core.interfaces.FragmentResultListener
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.databinding.FragmentCustomersBinding
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.presentation.libs.ActionDialog
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnActionClick
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnCallListener
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnItemClickListener
import javax.inject.Inject

@AndroidEntryPoint
class CustomersFragment : BaseFragment<FragmentCustomersBinding>(
	layoutResId = R.layout.fragment_customers,
	titleResId = R.string.customers,
	isShowBackButton = false
), OnItemClickListener<Customer>, FragmentResultListener, OnCallListener, OnActionClick<Customer> {
	@Inject
	lateinit var logger: Logger

	private val customerAdapter = CustomersAdapter().apply {
		onItemClickListener = this@CustomersFragment
		onCallListener = this@CustomersFragment
	}

	private val viewModel: CustomersViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setFragmentResultListener(FragmentResults.Customer.STORE, ::initFragmentResultListener)
		setFragmentResultListener(FragmentResults.Customer.UPDATE, ::initFragmentResultListener)

		setupObservers()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		setupAdapter()

		binding.fabAddCustomer.setOnClickListener {
			navTo(CustomersFragmentDirections.toCustomerCreate())
		}

		viewModel.fetchCustomersIfNeeded()
	}

	override fun getMainView(): ViewGroup? {
		return binding.main
	}

	override fun onItemClick(item: Customer, position: Int) {
		ActionDialog<Customer>(requireContext(), item, position, this)
			.show()
	}

	override fun onEdit(item: Customer, position: Int) {
		navTo(CustomersFragmentDirections.toCustomerEdit(item, position))
	}

	override fun onDelete(item: Customer, position: Int) {
		MaterialAlertDialogBuilder(requireContext())
			.setTitle(R.string.delete)
			.setMessage(getString(R.string.sure_delete, item.name))
			.setPositiveButton(R.string.yes) { dialog, _ ->
				dialog.dismiss()
				viewModel.destroyCustomer(item, position)
			}
			.setNegativeButton(R.string.close) { dialog, _ ->
				dialog.dismiss()
			}
			.show()
	}

	override fun onCall(phone: String) {
		PhoneHelper.makeCall(requireContext(), phone)
	}

	override fun initFragmentResultListener(key: String, bundle: Bundle) {
		if (key.equals(FragmentResults.Customer.STORE)) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				bundle.getParcelable("item", Customer::class.java)
			} else {
				@Suppress("DEPRECATION")
				bundle.getParcelable("item")
			}?.let { customer ->
				if (customerAdapter.addItem(customer)) {
					toggleEmptyList(false)
				}
			}
		} else if (key.equals(FragmentResults.Customer.UPDATE)) {
			val position = bundle.getInt("position")

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				bundle.getParcelable("item", Customer::class.java)
			} else {
				@Suppress("DEPRECATION")
				bundle.getParcelable("item")
			}?.let { customer ->
				customerAdapter.updateItem(position, customer)
			}
		}
	}

	private fun setupAdapter() {
		binding.rvCustomers.apply {
			layoutManager = LinearLayoutManager(requireContext())
			setHasFixedSize(true)
			adapter = customerAdapter
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
			observe(destroy, ::initDestroy)
		}
	}

	private fun initCustomers(result: ResponseResult.Success<List<Customer>>?) {
		result?.let {
			logger.debug("data is -> ${result.data}", TAG)
			customerAdapter.submitList(result.data)
		}
	}

	private fun initDestroy(position: Int) {
		if (position >= 0) {
			if (customerAdapter.removeItem(position)) {
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