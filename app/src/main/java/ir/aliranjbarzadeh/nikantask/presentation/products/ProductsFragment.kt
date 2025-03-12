package ir.aliranjbarzadeh.nikantask.presentation.products

import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.databinding.FragmentCustomersBinding

@AndroidEntryPoint
class ProductsFragment : BaseFragment<FragmentCustomersBinding>(
	layoutResId = R.layout.fragment_products,
	titleResId = R.string.products,
	isShowBackButton = false
) {
}