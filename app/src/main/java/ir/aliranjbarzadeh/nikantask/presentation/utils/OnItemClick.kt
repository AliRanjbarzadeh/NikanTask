package ir.aliranjbarzadeh.nikantask.presentation.utils

interface OnItemClick<T> {
	fun onItemClick(item: T, position: Int)
}