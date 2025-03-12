package ir.aliranjbarzadeh.nikantask.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.databinding.LoadingBinding

abstract class BaseFragment<VDB : ViewDataBinding>(
	@LayoutRes private val layoutResId: Int,
	@StringRes private val titleResId: Int,
	private val isShowBackButton: Boolean,
) : Fragment() {

	lateinit var binding: VDB

	private lateinit var loadingBinding: LoadingBinding

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)

		//Set title
		requireActivity().setTitle(titleResId)

		//Set background of fragment
		binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.md_theme_background))

		//Show or hide back button
		toggleBackButton(isShowBackButton)

		//Loading
		loadingBinding = LoadingBinding.inflate(layoutInflater, null, false)

		return binding.root
	}

	protected open fun getMainView(): ViewGroup? = null

	protected fun initLoading(isLoading: Boolean) {
		getMainView()?.also {
			toggleLoading(isLoading, it)
		}
	}

	private fun toggleLoading(isShow: Boolean, parentView: ViewGroup) {
		if (isShow) {
			parentView.addView(loadingBinding.root)
		} else {
			try {
				parentView.removeView(loadingBinding.root)
			} catch (_: Exception) {
			}
		}
	}

	private fun toggleBackButton(isShow: Boolean) {
		(requireActivity() as AppCompatActivity).supportActionBar?.also {
			it.setDisplayHomeAsUpEnabled(isShow)
			it.setDisplayShowHomeEnabled(isShow)
		}
	}

	fun back() {
		findNavController().popBackStack()
	}
}