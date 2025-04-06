package ir.aliranjbarzadeh.nikantask.core.utilities

import android.content.Context
import android.util.Log
import ir.aliranjbarzadeh.nikantask.core.helpers.PackageHelper
import javax.inject.Inject

class LoggerImpl @Inject constructor(private val context: Context) : Logger {
	override fun error(msg: Any?, tag: String?) {
		if (PackageHelper.isDebuggable(context)) {
			Log.e(tag ?: "", msg.toString())
		}
	}

	override fun debug(msg: Any?, tag: String?) {
		if (PackageHelper.isDebuggable(context)) {
			Log.d(tag ?: "", msg.toString())
		}
	}

	override fun info(msg: Any?, tag: String?) {
		if (PackageHelper.isDebuggable(context)) {
			Log.i(tag ?: "", msg.toString())
		}
	}
}