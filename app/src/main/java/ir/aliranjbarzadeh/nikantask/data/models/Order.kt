package ir.aliranjbarzadeh.nikantask.data.models

import android.os.Parcelable
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Order(
	var id: Long = 0,
	var customerId: Long,
	var createdAt: Date,
) : Parcelable {

	companion object {
		fun forDatabase(customerId: Long, createdAt: Date): Order {
			return Order(customerId = customerId, createdAt = createdAt)
		}
	}

	fun getCreatedAtJalali(): String = DateTimeHelper.formatDateTime(createdAt, persianFormat = "j F Y")

	override fun toString(): String {
		return "Order(id=$id, customerId=$customerId, createdAt=$createdAt)"
	}
}