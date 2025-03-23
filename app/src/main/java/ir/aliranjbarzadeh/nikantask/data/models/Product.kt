package ir.aliranjbarzadeh.nikantask.data.models

import android.os.Parcelable
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Product(
	var id: Long = 0,
	var name: String,
	var code: String,
	var createdAt: Date,
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

	override fun toString(): String {
		return "Product(id=$id, name='$name', code='$code', createdAt=$createdAt)"
	}
}
