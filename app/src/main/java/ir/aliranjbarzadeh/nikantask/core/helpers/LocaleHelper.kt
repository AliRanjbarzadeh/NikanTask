package ir.aliranjbarzadeh.nikantask.core.helpers

import android.content.Context
import android.content.res.Configuration
import ir.aliranjbarzadeh.nikantask.core.AppConfig
import ir.aliranjbarzadeh.nikantask.core.extensions.saveToSp
import java.util.Locale


object LocaleHelper {

	fun getLocale(): Locale {
		return Locale.getDefault()
	}

	fun onAttach(context: Context, language: String): Context {
		return setLocale(context, language)
	}

	fun setLocale(context: Context, language: String): Context {
		saveToSp(AppConfig.SessionKeys.LANGUAGE, language)
		return updateResources(context, language)
	}

	private fun updateResources(context: Context, language: String): Context {
		val locale = Locale(language)
		Locale.setDefault(locale)

		val configuration: Configuration = context.resources.configuration
		configuration.setLocale(locale)
		configuration.setLayoutDirection(locale)

		return context.createConfigurationContext(configuration)
	}
}