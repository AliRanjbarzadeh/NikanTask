package ir.aliranjbarzadeh.nikantask.data.models

import android.os.Parcelable
import androidx.databinding.ObservableBoolean
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Customer(
	var id: Long = 0,
	var name: String,
	var mobile: String,
	var createdAt: Date,
	private var _isChecked: Boolean = false,
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

	@IgnoredOnParcel
	var isChecked = ObservableBoolean(_isChecked)

	override fun toString(): String {
		return "Customer(id=$id, name='$name', mobile='$mobile', isChecked=$_isChecked, createdAt=$createdAt)"
	}
}
