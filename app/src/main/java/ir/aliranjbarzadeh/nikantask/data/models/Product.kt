package ir.aliranjbarzadeh.nikantask.data.models

import android.os.Parcelable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Product(
	var id: Long = 0,
	var name: String,
	var code: String,
	var createdAt: Date,
	private var _quantity: Int = 0,
) : Parcelable {
	companion object {
		fun forDatabase(name: String, code: String, createdAt: Date): Product {
			return Product(
				name = name,
				code = code,
				createdAt = createdAt
			)
		}
	}

	fun getCreatedAtJalali(): String = DateTimeHelper.formatDateTime(createdAt, persianFormat = "j F Y")

	@IgnoredOnParcel
	val isInCart = ObservableBoolean(_quantity > 0)

	@IgnoredOnParcel
	var quantity = ObservableField<String>(if (_quantity > 99) "+99" else _quantity.toString())

	fun setNewQuantity(newQuantity: Int) {
		_quantity = newQuantity
		quantity.set(if (_quantity > 99) "+99" else _quantity.toString())
		isInCart.set(newQuantity > 0)
	}

	fun getQuantityInt() = _quantity

	override fun toString(): String {
		return "Product(id=$id, name='$name', code='$code', quantity=$_quantity, createdAt=$createdAt)"
	}
}
