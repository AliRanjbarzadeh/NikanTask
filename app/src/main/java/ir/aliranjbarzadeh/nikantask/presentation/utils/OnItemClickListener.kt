package ir.aliranjbarzadeh.nikantask.presentation.utils

import android.view.View

interface OnItemClickListener<T> {
	fun onItemClick(item: T, position: Int)
}