package ir.aliranjbarzadeh.nikantask.presentation.orders.customer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ir.aliranjbarzadeh.nikantask.core.BaseHolder
import ir.aliranjbarzadeh.nikantask.core.BaseListAdapter
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import ir.aliranjbarzadeh.nikantask.databinding.SelectCustomerTemplateBinding
import ir.aliranjbarzadeh.nikantask.presentation.utils.CheckableList
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnCallListener
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnItemClick

class SelectCustomerAdapter : BaseListAdapter<Customer>(DIFF_CALLBACK), CheckableList<Customer> {

	lateinit var onItemClick: OnItemClick<Customer>
	lateinit var onCallListener: OnCallListener

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Customer>() {
			override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
				return oldItem.name == newItem.name
						&& oldItem.mobile == newItem.mobile
						&& oldItem.isChecked.get() == newItem.isChecked.get()
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Customer> {
		val binding = SelectCustomerTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)

		return object : BaseHolder<Customer>(binding) {
			override fun onBindUI(item: Customer) {
				binding.item = item

				binding.root.setOnClickListener { onItemClick.onItemClick(item, adapterPosition) }

				binding.btnCall.setOnClickListener {
					onCallListener.onCall(item.mobile)
				}

				binding.executePendingBindings()
			}
		}
	}

	override fun changeChecked(position: Int) {
		val oldPosition = currentList.indexOfFirst { it.isChecked.get() == true }
		if (oldPosition >= 0) {
			currentList[oldPosition].isChecked.set(false)
		}

		currentList[position].isChecked.set(true)
	}

	override fun getChecked(): Customer? {
		return currentList.find { it.isChecked.get() == true }
	}
}