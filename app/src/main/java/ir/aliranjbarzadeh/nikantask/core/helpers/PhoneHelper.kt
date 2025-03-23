package ir.aliranjbarzadeh.nikantask.core.helpers

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

object PhoneHelper {
	fun makeCall(context: Context, phone: String) {
		try {
			val intent = Intent(Intent.ACTION_DIAL)
			intent.data = "tel:$phone".toUri()
			context.startActivity(intent)
		} catch (_: Exception) {
		}
	}
}