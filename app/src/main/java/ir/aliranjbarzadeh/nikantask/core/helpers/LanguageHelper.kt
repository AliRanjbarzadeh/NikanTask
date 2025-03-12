package ir.aliranjbarzadeh.nikantask.core.helpers

import ir.aliranjbarzadeh.nikantask.core.AppConfig
import ir.aliranjbarzadeh.nikantask.core.extensions.loadFromSp


object LanguageHelper {
	fun getLanguage(): String {
		return loadFromSp(AppConfig.SessionKeys.LANGUAGE, AppConfig.Defaults.LANGUAGE)
	}

	fun isDefault(language: String): Boolean {
		return language == AppConfig.Defaults.LANGUAGE
	}
}