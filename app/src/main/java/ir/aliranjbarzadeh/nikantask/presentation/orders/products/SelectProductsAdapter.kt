package ir.aliranjbarzadeh.nikantask.presentation.orders.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ir.aliranjbarzadeh.nikantask.core.BaseHolder
import ir.aliranjbarzadeh.nikantask.core.BaseListAdapter
import ir.aliranjbarzadeh.nikantask.data.models.Product
import ir.aliranjbarzadeh.nikantask.databinding.SelectProductTemplateBinding
import ir.aliranjbarzadeh.nikantask.presentation.utils.OnCartActions

class SelectProductsAdapter : BaseListAdapter<Product>(DIFF_CALLBACK) {

	lateinit var onCartActions: OnCartActions<Product>

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
			override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
				return oldItem.getQuantityInt() == newItem.getQuantityInt()
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<Product> {
		val binding = SelectProductTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)

		return object : BaseHolder<Product>(binding) {
			override fun onBindUI(item: Product) {
				binding.item = item

				binding.btnAddToCart.setOnClickListener { onCartActions.onAddToCart(item, adapterPosition) }

				binding.btnEditCartItem.setOnClickListener { onCartActions.onEditCartItem(item, adapterPosition) }

				binding.btnDeleteFromCart.setOnClickListener { onCartActions.onDeleteFromCart(item, adapterPosition) }

				binding.executePendingBindings()
			}
		}
	}

	fun getShoppingProducts(): List<Product> {
		return currentList.filter { it.isInCart.get() == true }
	}
}