package ir.aliranjbarzadeh.nikantask.data.models

import android.os.Parcelable
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Customer(
	var id: Long = 0,
	var name: String,
	var mobile: String,
	var createdAt: Date,
) : Parcelable {
	companion object {
		fun forDatabase(name: String, mobile: String, createdAt: Date): Customer {
			return Customer(
				name = name,
				mobile = mobile,
				createdAt = createdAt
			)
		}
	}

	fun getCreatedAtJalali(): String = DateTimeHelper.formatDateTime(createdAt, persianFormat = "j F Y")

	override fun toString(): String {
		return "Customer(id=$id, name='$name', mobile='$mobile', createdAt=$createdAt)"
	}
}
