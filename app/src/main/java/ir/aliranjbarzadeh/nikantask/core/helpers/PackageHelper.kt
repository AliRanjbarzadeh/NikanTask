package ir.aliranjbarzadeh.nikantask.core.helpers

import android.content.Context
import android.content.pm.ApplicationInfo


object PackageHelper {
	fun isDebuggable(context: Context): Boolean {
		return context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
	}
}