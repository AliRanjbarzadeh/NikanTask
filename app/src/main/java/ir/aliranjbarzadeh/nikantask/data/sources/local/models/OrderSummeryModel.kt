package ir.aliranjbarzadeh.nikantask.data.sources.local.models

import androidx.room.Embedded
import ir.aliranjbarzadeh.nikantask.data.interfaces.ResponseObject
import ir.aliranjbarzadeh.nikantask.data.models.OrderSummery

data class OrderSummeryModel(
	@Embedded(prefix = "order_")
	val orderModel: OrderModel,

	@Embedded(prefix = "customer_")
	val customerModel: CustomerModel,

	val totalItems: Int,
) : ResponseObject<OrderSummery> {
	override fun toDomain() = OrderSummery(
		orderModel.toDomain(),
		customerModel.toDomain(),
		totalItems
	)
}