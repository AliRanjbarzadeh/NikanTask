package ir.aliranjbarzadeh.nikantask.core.di

import android.content.Context
import ir.aliranjbarzadeh.nikantask.core.AppConfig
import ir.aliranjbarzadeh.nikantask.core.helpers.PackageHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseNetwork @Inject constructor(context: Context) {
	val baseUrl: String = if (PackageHelper.isDebuggable(context)) {
		AppConfig.Defaults.BASE_URL_DEV
	} else {
		AppConfig.Defaults.BASE_URL
	}
}