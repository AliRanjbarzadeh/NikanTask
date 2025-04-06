package ir.aliranjbarzadeh.nikantask.presentation.customers.edit

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.core.FragmentResults
import ir.aliranjbarzadeh.nikantask.core.extensions.observe
import ir.aliranjbarzadeh.nikantask.core.helpers.KeyboardHelper
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.databinding.FragmentCustomerEditBinding
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import javax.inject.Inject

@AndroidEntryPoint
class CustomerEditFragment : BaseFragment<FragmentCustomerEditBinding>(
	layoutResId = R.layout.fragment_customer_edit,
	titleResId = R.string.edit_customer,
	isShowBackButton = true
) {
	@Inject
	lateinit var logger: Logger

	private val viewModel: CustomerEditViewModel by viewModels()
	private val args: CustomerEditFragmentArgs by navArgs()
	private var position: Int = -1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel.setCustomer(args.customer)
		position = args.position

		setupObservers()
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.viewModel = viewModel

		binding.etFullName.doAfterTextChanged {
			binding.tilFullName.isErrorEnabled = it.isNullOrEmpty()
			if (it.isNullOrEmpty())
				binding.tilFullName.error = getString(R.string.enter_full_name)
		}

		binding.etMobile.doAfterTextChanged {
			binding.tilMobile.isErrorEnabled = it.isNullOrEmpty()
			if (it.isNullOrEmpty())
				binding.tilMobile.error = getString(R.string.enter_mobile)
		}

		//Setup keyboard helper to clear focus on keyboard state change
		KeyboardHelper.init(
			requireActivity(),
			listOf(
				binding.etFullName, binding.etMobile
			)
		)

		binding.btnSave.setOnClickListener {
			var hasError = false
			if (viewModel.fullName.value.isNullOrEmpty()) {
				binding.tilFullName.error = getString(R.string.enter_full_name)
				hasError = true
			}

			if (viewModel.mobile.value.isNullOrEmpty()) {
				binding.tilMobile.error = getString(R.string.enter_mobile)
				hasError = true
			}

			if (hasError)
				return@setOnClickListener

			KeyboardHelper.hideKeyboard(binding.btnSave)
			viewModel.update()
		}
	}

	private fun setupObservers() {
		viewModel.run {
			observe(isLoading, ::initLoading)
			observe(update, ::initUpdate)
			observe(error, ::initError)
		}
	}

	private fun initUpdate(result: ResponseResult.Success<Customer>?) {
		result?.let {
			setFragmentResult(FragmentResults.Customer.UPDATE, bundleOf("item" to result.data, "position" to position))
			back()
		}
	}

	private fun initError(result: ResponseResult.Error?) {
		result?.let {
			logger.error("error is -> ${result.data.statusCode}", TAG)

			MaterialAlertDialogBuilder(requireContext())
				.setTitle(R.string.error)
				.setMessage(result.data.message)
				.setPositiveButton(R.string.try_again) { dialog, _ ->
					viewModel.update()
					dialog.dismiss()
				}
				.setNegativeButton(R.string.close) { dialog, _ ->
					dialog.dismiss()
				}
		}
	}
}