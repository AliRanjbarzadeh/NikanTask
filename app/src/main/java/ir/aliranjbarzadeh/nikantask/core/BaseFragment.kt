package ir.aliranjbarzadeh.nikantask.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.databinding.LoadingBinding
import ir.aliranjbarzadeh.nikantask.databinding.TemplateEmptyListBinding

abstract class BaseFragment<VDB : ViewDataBinding>(
	@LayoutRes private val layoutResId: Int,
	@StringRes private val titleResId: Int,
	private val isShowBackButton: Boolean,
) : Fragment() {
	protected val TAG = "${this::class.java.simpleName.replace("Fragment", "")}Log"

	lateinit var binding: VDB

	private lateinit var loadingBinding: LoadingBinding

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)

		setTitle()

		setBackgroundColor()

		toggleBackButton(isShowBackButton)

		prepareLoadingView()

		return binding.root
	}

	private fun setTitle() {
		requireActivity().setTitle(titleResId)
	}

	private fun setBackgroundColor() {
		binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.md_theme_background))
	}

	private fun prepareLoadingView() {
		loadingBinding = LoadingBinding.inflate(layoutInflater, null, false)
	}

	protected open fun getMainView(): ViewGroup? = null

	protected fun initLoading(isLoading: Boolean?) {
		isLoading?.let {
			getMainView()?.also { mainView ->
				toggleLoading(isLoading, mainView)
			}
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

	protected open fun initEmptyList(isEmptyList: Boolean?) {
		isEmptyList?.let { toggleEmptyList(isEmptyList) }
	}

	protected fun toggleEmptyList(isEmptyList: Boolean) {
		getMainView()?.also { mainView ->
			if (isEmptyList) {
				val emptyListBinding = TemplateEmptyListBinding.inflate(layoutInflater, mainView, false)
				mainView.addView(emptyListBinding.root)
			} else {
				val emptyListView = mainView.findViewById<ConstraintLayout>(R.id.cl_empty_list)
				emptyListView?.let {
					mainView.removeView(emptyListView)
				}
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