package ir.aliranjbarzadeh.nikantask.core

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseHolder<T>>() {

	var mItems: MutableList<T> = mutableListOf()
		set(value) {
			field = value
			notifyItemRangeInserted(0, field.size)
		}

	override fun getItemCount(): Int {
		return mItems.size
	}

	override fun onBindViewHolder(holder: BaseHolder<T>, position: Int) {
		holder.onBindUI(mItems[position], position)
	}
}