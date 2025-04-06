package ir.aliranjbarzadeh.nikantask.data.sources.local.models

import androidx.room.Embedded
import ir.aliranjbarzadeh.nikantask.data.interfaces.ResponseObject
import ir.aliranjbarzadeh.nikantask.data.models.Product

data class ProductWithQuantityModel(
	@Embedded
	val productModel: ProductModel,
	val quantity: Int,
) : ResponseObject<Product> {
	override fun toDomain() = productModel.toDomain().apply {
		setNewQuantity(this@ProductWithQuantityModel.quantity)
	}
}
