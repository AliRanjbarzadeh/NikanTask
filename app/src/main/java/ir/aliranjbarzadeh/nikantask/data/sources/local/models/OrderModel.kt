package ir.aliranjbarzadeh.nikantask.data.sources.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import ir.aliranjbarzadeh.nikantask.data.interfaces.ResponseObject
import ir.aliranjbarzadeh.nikantask.data.models.Order
import java.util.Date

@Entity(
	tableName = "orders"
)
data class OrderModel(
	@PrimaryKey(autoGenerate = true)
	val orderId: Long = 0,
	val customerRefId: Long,
	val createdAt: Date,
	val updatedAt: Date,
	val deletedAt: Date? = null,
) : ResponseObject<Order> {

	companion object {
		fun fromModel(order: Order): OrderModel {
			val date = DateTimeHelper.currentDateUTC()
			return OrderModel(
				orderId = order.id,
				customerRefId = order.customerId,
				createdAt = order.createdAt,
				updatedAt = date,
			)
		}
	}

	override fun toDomain() = Order(
		id = orderId,
		customerId = customerRefId,
		createdAt = createdAt
	)
}