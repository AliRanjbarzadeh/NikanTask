package ir.aliranjbarzadeh.nikantask.presentation.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ir.aliranjbarzadeh.nikantask.core.BaseHolder
import ir.aliranjbarzadeh.nikantask.core.BaseListAdapter
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.databinding.ProductTemplateBinding
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnItemClickListener

class ProductsAdapter : BaseListAdapter<Product>(DIFF_CALLBACK) {

	lateinit var onItemClickListener: OnItemClickListener<Product>

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
			override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
				return oldItem.name.equals(newItem.name) && oldItem.code.equals(newItem.code)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Product> {
		val binding = ProductTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)

		return object : BaseHolder<Product>(binding) {
			override fun onBindUI(item: Product, position: Int) {
				binding.item = item

				binding.imgActions.setOnClickListener { onItemClickListener.onItemClick(item, bindingAdapterPosition) }

				binding.executePendingBindings()
			}
		}
	}
}