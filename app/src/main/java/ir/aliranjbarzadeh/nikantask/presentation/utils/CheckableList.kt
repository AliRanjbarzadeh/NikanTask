package ir.aliranjbarzadeh.nikantask.presentation.utils

interface CheckableList<T> {
	fun changeChecked(position: Int)

	fun getChecked(): T?
}