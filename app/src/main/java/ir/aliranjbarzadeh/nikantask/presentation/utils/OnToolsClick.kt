package ir.aliranjbarzadeh.nikantask.presentation.utils

interface OnToolsClick<T> {
	fun onToolsClick(item: T, position: Int)
}