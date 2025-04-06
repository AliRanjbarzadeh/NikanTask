package ir.aliranjbarzadeh.nikantask.data.sources.local.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import ir.aliranjbarzadeh.nikantask.data.models.OrderProduct

@Entity(
	tableName = "order_products",
	primaryKeys = ["orderRefId", "productRefId"],
	foreignKeys = [
		ForeignKey(entity = OrderModel::class, parentColumns = ["orderId"], childColumns = ["orderRefId"]),
		ForeignKey(entity = ProductModel::class, parentColumns = ["productId"], childColumns = ["productRefId"]),
	],
	indices = [
		Index(value = ["orderRefId"]),
		Index(value = ["productRefId"])
	]
)
data class OrderProductPivot(
	val productRefId: Long,
	val quantity: Int,
	val orderRefId: Long = 0L,
) {
	companion object {
		fun fromModel(orderProduct: OrderProduct) = OrderProductPivot(
			productRefId = orderProduct.productId,
			quantity = orderProduct.quantity,
			orderRefId = orderProduct.orderId,
		)
	}
}