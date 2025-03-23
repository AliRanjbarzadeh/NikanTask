package ir.aliranjbarzadeh.nikantask.core

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<T>(
	diffCallback: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, BaseHolder<T>>(diffCallback) {

	override fun onBindViewHolder(holder: BaseHolder<T>, position: Int) {
		getItem(position)?.let { item ->
			holder.onBindUI(item, position)
		}
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
	}

	fun removeItem(position: Int): Boolean {
		val newList = currentList.toMutableList()
		newList.removeAt(position)
		submitList(newList)
		return newList.isEmpty()
	}
}