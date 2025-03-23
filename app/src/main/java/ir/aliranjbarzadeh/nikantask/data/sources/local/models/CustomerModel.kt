package ir.aliranjbarzadeh.nikantask.data.sources.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import ir.aliranjbarzadeh.nikantask.data.interfaces.ResponseObject
import ir.aliranjbarzadeh.nikantask.data.models.Customer
import java.util.Date

@Entity(
	tableName = "customers"
)
class CustomerModel(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,

	@ColumnInfo(name = "name")
	val name: String,

	@ColumnInfo(name = "mobile")
	val mobile: String,

	@ColumnInfo(name = "created_at")
	val createdAt: Date,

	@ColumnInfo(name = "updated_at")
	val updatedAt: Date,

	@ColumnInfo(name = "deleted_at")
	val deletedAt: Date? = null,
) : ResponseObject<Customer> {

	companion object {
		fun fromModel(customer: Customer): CustomerModel {
			val date = DateTimeHelper.currentDateUTC()
			return CustomerModel(
				id = customer.id,
				name = customer.name,
				mobile = customer.mobile,
				createdAt = customer.createdAt,
				updatedAt = date,
			)
		}
	}

	override fun toDomain() = Customer(
		id = id,
		name = name,
		mobile = mobile,
		createdAt = createdAt,
	)
}