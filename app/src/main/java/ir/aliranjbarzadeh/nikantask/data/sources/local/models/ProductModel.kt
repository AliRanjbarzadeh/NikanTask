package ir.aliranjbarzadeh.nikantask.data.sources.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import ir.aliranjbarzadeh.nikantask.data.interfaces.ResponseObject
import ir.aliranjbarzadeh.nikantask.data.models.Product
import java.util.Date

@Entity(
	tableName = "products"
)
class ProductModel(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,

	@ColumnInfo(name = "name")
	val name: String,

	@ColumnInfo(name = "code")
	val code: String,

	@ColumnInfo(name = "created_at")
	val createdAt: Date,

	@ColumnInfo(name = "updated_at")
	val updatedAt: Date,

	@ColumnInfo(name = "deleted_at")
	val deletedAt: Date? = null,
) : ResponseObject<Product> {

	companion object {
		fun fromModel(product: Product): ProductModel {
			val date = DateTimeHelper.currentDateUTC()
			return ProductModel(
				id = product.id,
				name = product.name,
				code = product.code,
				createdAt = product.createdAt,
				updatedAt = date,
			)
		}
	}

	override fun toDomain() = Product(
		id = id,
		name = name,
		code = code,
		createdAt = createdAt,
	)
}