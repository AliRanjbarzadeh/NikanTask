package ir.aliranjbarzadeh.nikantask.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import ir.aliranjbarzadeh.nikantask.R

abstract class BaseViewPagerChildFragment<VDB : ViewDataBinding>(
	@LayoutRes private val layoutResId: Int,
) : Fragment() {
	lateinit var binding: VDB

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)

		//set background of fragment
		binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.md_theme_background))

		return binding.root
	}
}