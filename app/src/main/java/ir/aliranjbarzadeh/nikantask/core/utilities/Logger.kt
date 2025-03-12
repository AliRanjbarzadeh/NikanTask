package ir.aliranjbarzadeh.nikantask.core.utilities

import android.content.Context
import android.util.Log
import ir.aliranjbarzadeh.nikantask.core.helpers.PackageHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Logger @Inject constructor(private val context: Context) {
	fun error(msg: Any?, tag: String = "") {
		if (PackageHelper.isDebuggable(context)) {
			Log.e(tag, msg.toString())
		}
	}

	fun debug(msg: Any?, tag: String = "") {
		if (PackageHelper.isDebuggable(context)) {
			Log.d(tag, msg.toString())
		}
	}

	fun info(msg: Any?, tag: String = "") {
		if (PackageHelper.isDebuggable(context)) {
			Log.i(tag, msg.toString())
		}
	}
}