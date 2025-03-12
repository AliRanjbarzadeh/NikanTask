package ir.aliranjbarzadeh.nikantask.presentation.orders

import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseFragment
import ir.aliranjbarzadeh.nikantask.databinding.FragmentCustomersBinding
import ir.aliranjbarzadeh.nikantask.databinding.FragmentOrdersBinding

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding>(
	layoutResId = R.layout.fragment_orders,
	titleResId = R.string.orders,
	isShowBackButton = false
) {
}