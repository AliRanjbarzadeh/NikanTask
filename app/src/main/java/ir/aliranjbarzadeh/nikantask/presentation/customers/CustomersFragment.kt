package ir.aliranjbarzadeh.nikantask.presentation.customers

import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.databinding.FragmentCustomersBinding

@AndroidEntryPoint
class CustomersFragment : BaseFragment<FragmentCustomersBinding>(
	layoutResId = R.layout.fragment_customers,
	titleResId = R.string.customers,
	isShowBackButton = false
) {
}