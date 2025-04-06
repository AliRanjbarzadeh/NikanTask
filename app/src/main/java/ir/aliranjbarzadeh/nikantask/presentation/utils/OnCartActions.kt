package ir.aliranjbarzadeh.nikantask.presentation.utils

interface OnCartActions<T> {
	fun onAddToCart(item: T, position: Int)
	fun onEditCartItem(item: T, position: Int)
	fun onDeleteFromCart(item: T, position: Int)
}