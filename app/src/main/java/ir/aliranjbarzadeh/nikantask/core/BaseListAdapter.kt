package ir.aliranjbarzadeh.nikantask.core

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<T>(
	diffCallback: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, BaseHolder<T>>(diffCallback) {

	override fun onBindViewHolder(holder: BaseHolder<T>, position: Int) {
		getItem(position)?.let { item ->
			holder.onBindUI(item)
		}
	}

	fun addItems(newItems: List<T>) {
		val combined = currentList.toMutableList().apply {
			addAll(newItems)
		}
		submitList(combined)
	}

	fun addItem(item: T): Boolean {
		val newList = currentList.toMutableList()
		newList.add(0, item)
		submitList(newList)
		return newList.size == 1
	}

	fun updateItem(position: Int, item: T) {
		val newList = currentList.toMutableList()
		newList[position] = item
		submitList(newList)
		notifyItemChanged(position)
	}

	fun removeItem(position: Int): Boolean {
		val newList = currentList.toMutableList()
		newList.removeAt(position)
		submitList(newList)
		return newList.isEmpty()
	}

	fun clearItems() {
		val newList = currentList.toMutableList()
		newList.clear()
		submitList(newList)
	}
}