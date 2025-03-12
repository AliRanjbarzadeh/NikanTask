package ir.aliranjbarzadeh.nikantask.presentation.customers

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.core.extensions.observe
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

	override fun getMainView(): ViewGroup? {
		return binding.main
	}

	private fun setupObservers() {
		viewModel.run {
			observe(isLoading, ::initLoading)
			observe(isEmptyList, ::initEmptyList)
			observe(items, ::initCustomers)
			observe(error, ::initError)
		}
	}

	private fun initCustomers(result: ResponseResult.Success<List<Customer>>?) {
		logger.debug(result?.data, TAG)
	}

	private fun initError(result: ResponseResult.Error?) {
		logger.error(result?.data?.message, TAG)
		logger.error(result?.data?.statusCode, TAG)
	}
}