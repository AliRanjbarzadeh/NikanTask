package ir.aliranjbarzadeh.nikantask.presentation.customers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ir.aliranjbarzadeh.nikantask.core.BaseHolder
import ir.aliranjbarzadeh.nikantask.core.BaseListAdapter
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.databinding.CustomerTemplateBinding
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnCallListener
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnItemClickListener

class CustomersAdapter : BaseListAdapter<Customer>(DIFF_CALLBACK) {

	lateinit var onItemClickListener: OnItemClickListener<Customer>
	lateinit var onCallListener: OnCallListener

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Customer>() {
			override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
				return oldItem.name.equals(newItem.name) && oldItem.mobile.equals(newItem.mobile)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Customer> {
		val binding = CustomerTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)

		return object : BaseHolder<Customer>(binding) {
			override fun onBindUI(item: Customer, position: Int) {
				binding.item = item

				binding.imgActions.setOnClickListener { onItemClickListener.onItemClick(item, bindingAdapterPosition) }

				binding.btnCall.setOnClickListener {
					onCallListener.onCall(item.mobile)
				}

				binding.executePendingBindings()
			}
		}
	}
}