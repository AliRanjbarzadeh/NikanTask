package ir.aliranjbarzadeh.nikantask.presentation.products.create

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.core.FragmentResults
import ir.aliranjbarzadeh.nikantask.core.extensions.observe
import ir.aliranjbarzadeh.nikantask.core.helpers.KeyboardHelper
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.databinding.FragmentProductCreateBinding
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import javax.inject.Inject

@AndroidEntryPoint
class ProductCreateFragment : BaseFragment<FragmentProductCreateBinding>(
	layoutResId = R.layout.fragment_product_create,
	titleResId = R.string.add_product,
	isShowBackButton = true
) {
	@Inject
	lateinit var logger: Logger

	private val viewModel: ProductCreateViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setupObservers()
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.viewModel = viewModel

		//Go to next input on click next on keyboard
		binding.etName.setOnEditorActionListener { _, actionId, _ ->
			if (actionId == EditorInfo.IME_ACTION_NEXT) {
				binding.etCode.requestFocus()
				true
			} else {
				false
			}
		}

		binding.etName.doAfterTextChanged {
			binding.tilName.isErrorEnabled = it.isNullOrEmpty()
			if (it.isNullOrEmpty())
				binding.tilName.error = getString(R.string.enter_full_name)
		}

		binding.etCode.doAfterTextChanged {
			binding.tilCode.isErrorEnabled = it.isNullOrEmpty()
			if (it.isNullOrEmpty())
				binding.tilCode.error = getString(R.string.enter_mobile)
		}

		//Setup keyboard helper to clear focus on keyboard state change
		KeyboardHelper.init(
			requireActivity(),
			listOf(
				binding.etName, binding.etCode
			)
		)

		binding.btnSave.setOnClickListener {
			var hasError = false
			if (viewModel.name.value.isNullOrEmpty()) {
				binding.tilName.error = getString(R.string.enter_full_name)
				hasError = true
			}

			if (viewModel.code.value.isNullOrEmpty()) {
				binding.tilCode.error = getString(R.string.enter_mobile)
				hasError = true
			}

			if (hasError)
				return@setOnClickListener

			KeyboardHelper.hideKeyboard(binding.btnSave)
			viewModel.store()
		}
	}

	private fun setupObservers() {
		viewModel.run {
			observe(isLoading, ::initLoading)
			observe(store, ::initStore)
			observe(error, ::initError)
		}
	}

	private fun initStore(result: ResponseResult.Success<Product>?) {
		result?.let {
			setFragmentResult(FragmentResults.Product.STORE, bundleOf("item" to result.data))
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
					viewModel.store()
					dialog.dismiss()
				}
				.setNegativeButton(R.string.close) { dialog, _ ->
					dialog.dismiss()
				}
		}
	}
}