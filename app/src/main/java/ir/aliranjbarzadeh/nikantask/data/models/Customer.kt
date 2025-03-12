package ir.aliranjbarzadeh.nikantask.data.models

import android.os.Parcelable
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Customer(
	val id: Long = 0,
	val name: String,
	val mobile: String,
	var createdAt: Date,
	var updatedAt: Date,
) : Parcelable {
	companion object {
		fun forCreate(name: String, mobile: String) {
			val date = DateTimeHelper.currentDateUTC()
			Customer(
				name = name, mobile = mobile, createdAt = date, updatedAt = date
			)
		}
	}
}
