package ir.aliranjbarzadeh.nikantask.data.sources.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import ir.aliranjbarzadeh.nikantask.data.interfaces.ResponseObject
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import java.util.Date

@Entity(
	tableName = "customers"
)
data class CustomerModel(
	@PrimaryKey(autoGenerate = true)
	var customerId: Long = 0,
	val name: String,
	val mobile: String,
	val createdAt: Date,
	val updatedAt: Date,
	val deletedAt: Date? = null,
) : ResponseObject<Customer> {

	companion object {
		fun fromModel(customer: Customer): CustomerModel {
			val date = DateTimeHelper.currentDateUTC()
			return CustomerModel(
				customerId = customer.id,
				name = customer.name,
				mobile = customer.mobile,
				createdAt = customer.createdAt,
				updatedAt = date,
			)
		}
	}

	override fun toDomain() = Customer(
		id = customerId,
		name = name,
		mobile = mobile,
		createdAt = createdAt,
	)
}