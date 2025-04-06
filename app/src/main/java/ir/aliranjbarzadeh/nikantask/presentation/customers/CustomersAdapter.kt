package ir.aliranjbarzadeh.nikantask.presentation.customers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ir.aliranjbarzadeh.nikantask.core.BaseHolder
import ir.aliranjbarzadeh.nikantask.core.BaseListAdapter
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.databinding.CustomerTemplateBinding
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnCallListener
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnToolsClick

class CustomersAdapter : BaseListAdapter<Customer>(DIFF_CALLBACK) {

	lateinit var onToolsClick: OnToolsClick<Customer>
	lateinit var onCallListener: OnCallListener

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Customer>() {
			override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
				return oldItem.name == newItem.name && oldItem.mobile == newItem.mobile
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Customer> {
		val binding = CustomerTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)

		return object : BaseHolder<Customer>(binding) {
			override fun onBindUI(item: Customer) {
				binding.item = item

				binding.btnActions.setOnClickListener { onToolsClick.onToolsClick(item, adapterPosition) }

				binding.btnCall.setOnClickListener {
					onCallListener.onCall(item.mobile)
				}

				binding.executePendingBindings()
			}
		}
	}
}