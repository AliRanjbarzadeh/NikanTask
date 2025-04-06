package ir.aliranjbarzadeh.nikantask.data.models

data class OrderProduct(
	val productId: Long,
	val quantity: Int,
	val orderId: Long = 0L,
)