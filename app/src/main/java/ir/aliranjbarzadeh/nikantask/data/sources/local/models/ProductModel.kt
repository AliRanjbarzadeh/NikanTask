package ir.aliranjbarzadeh.nikantask.data.sources.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import ir.aliranjbarzadeh.nikantask.data.interfaces.ResponseObject
import ir.aliranjbarzadeh.nikantask.data.models.Product
import java.util.Date

@Entity(
	tableName = "products"
)
data class ProductModel(
	@PrimaryKey(autoGenerate = true)
	var productId: Long = 0,
	val name: String,
	val code: String,
	val createdAt: Date,
	val updatedAt: Date,
	val deletedAt: Date? = null,
) : ResponseObject<Product> {

	companion object {
		fun fromModel(product: Product): ProductModel {
			val date = DateTimeHelper.currentDateUTC()
			return ProductModel(
				productId = product.id,
				name = product.name,
				code = product.code,
				createdAt = product.createdAt,
				updatedAt = date,
			)
		}
	}

	override fun toDomain() = Product(
		id = productId,
		name = name,
		code = code,
		createdAt = createdAt,
	)
}