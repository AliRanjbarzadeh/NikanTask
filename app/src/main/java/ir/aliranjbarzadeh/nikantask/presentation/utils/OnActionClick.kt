package ir.aliranjbarzadeh.nikantask.presentation.utils

interface OnActionClick<T> {
	fun onEdit(item: T, position: Int)
	fun onDelete(item: T, position: Int)
}