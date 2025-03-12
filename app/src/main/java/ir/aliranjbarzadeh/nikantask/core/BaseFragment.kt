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

abstract class BaseFragment<VDB : ViewDataBinding>(
	@LayoutRes private val layoutResId: Int,
	@StringRes private val titleResId: Int,
	private val isShowBackButton: Boolean,
) : Fragment() {
	lateinit var binding: VDB

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)

		//Set title
		requireActivity().setTitle(titleResId)

		//set background of fragment
		binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.md_theme_background))

		toggleBackButton(isShowBackButton)

		return binding.root
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