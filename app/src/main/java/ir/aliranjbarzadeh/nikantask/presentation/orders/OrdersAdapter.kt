package ir.aliranjbarzadeh.nikantask.presentation.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ir.aliranjbarzadeh.nikantask.core.BaseHolder
import ir.aliranjbarzadeh.nikantask.core.BaseListAdapter
import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery
import ir.aliranjbarzadeh.nikantask.databinding.OrderTemplateBinding
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnShowClick
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnToolsClick

class OrdersAdapter : BaseListAdapter<OrderSummery>(DIFF_CALLBACK) {

	lateinit var onToolsClick: OnToolsClick<OrderSummery>
	lateinit var onShowClick: OnShowClick<OrderSummery>

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderSummery>() {
			override fun areItemsTheSame(oldItem: OrderSummery, newItem: OrderSummery): Boolean {
				return oldItem.order.id == newItem.order.id
			}

			override fun areContentsTheSame(oldItem: OrderSummery, newItem: OrderSummery): Boolean {
				return oldItem.order.id == newItem.order.id && oldItem.order.customerId == newItem.order.customerId
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<OrderSummery> {
		val binding = OrderTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)

		return object : BaseHolder<OrderSummery>(binding) {
			override fun onBindUI(item: OrderSummery) {
				binding.item = item

				binding.btnActions.setOnClickListener { onToolsClick.onToolsClick(item, adapterPosition) }
				binding.root.setOnClickListener { onShowClick.onShow(item) }

				binding.executePendingBindings()
			}
		}
	}
}