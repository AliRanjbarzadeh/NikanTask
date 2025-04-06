package ir.aliranjbarzadeh.nikantask.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderSummery(
	val order: Order,
	val customer: Customer,
	val totalItems: Int,
) : Parcelable {
	override fun toString(): String {
		return "OrderSummery(order=$order, customer=$customer, totalItems=$totalItems)"
	}
}