package ir.aliranjbarzadeh.nikantask.core

object AppConfig {
	const val DATABASE = "shop_management"

	object Defaults {
		const val LANGUAGE = "fa"
		const val BASE_URL_DEV = ""
		const val BASE_URL = ""
	}

	object SessionKeys {
		const val LOGIN = "user.login"
		const val LANGUAGE = "user.language"
		const val SEEDER = "seeder"
	}
}