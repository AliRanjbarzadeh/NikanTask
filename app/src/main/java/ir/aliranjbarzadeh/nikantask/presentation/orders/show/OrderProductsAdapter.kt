package ir.aliranjbarzadeh.nikantask.presentation.orders.show

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ir.aliranjbarzadeh.nikantask.core.BaseHolder
import ir.aliranjbarzadeh.nikantask.core.BaseListAdapter
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.databinding.OrderShowProductTemplateBinding

class OrderProductsAdapter : BaseListAdapter<Product>(DIFF_CALLBACK) {

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
			override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
				return oldItem.name == newItem.name
						&& oldItem.code == newItem.code
						&& oldItem.getQuantityInt() == newItem.getQuantityInt()
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Product> {
		val binding = OrderShowProductTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)

		return object : BaseHolder<Product>(binding) {
			override fun onBindUI(item: Product) {
				binding.item = item

				binding.executePendingBindings()
			}
		}
	}
}