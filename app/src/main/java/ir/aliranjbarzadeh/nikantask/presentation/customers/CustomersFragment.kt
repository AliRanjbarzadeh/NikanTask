package ir.aliranjbarzadeh.nikantask.presentation.customers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.core.extensions.navTo
import ir.aliranjbarzadeh.nikantask.core.extensions.observe
import ir.aliranjbarzadeh.nikantask.core.extensions.repeatObserve
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.databinding.FragmentCustomersBinding
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import javax.inject.Inject

@AndroidEntryPoint
class CustomersFragment : BaseFragment<FragmentCustomersBinding>(
	layoutResId = R.layout.fragment_customers,
	titleResId = R.string.customers,
	isShowBackButton = false
) {
	@Inject
	lateinit var logger: Logger

	private val viewModel: CustomersViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setupObservers()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.fabAddCustomer.setOnClickListener {
			navTo(CustomersFragmentDirections.toAddCustomer())
		}

		viewModel.fetchCustomersIfNeeded()
	}

	override fun getMainView(): ViewGroup? {
		return binding.main
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
		}
	}

	private fun initError(result: ResponseResult.Error?) {
		result?.let {
			logger.error("error is -> ${result.data.message}", TAG)
			logger.error("error is -> ${result.data.statusCode}", TAG)
		}
	}
}