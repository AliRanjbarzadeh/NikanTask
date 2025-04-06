package ir.aliranjbarzadeh.nikantask.core

object AppConfig {
	const val DATABASE = "shop_management"

	object Defaults {
		const val LANGUAGE = "fa"
	}

	object SessionKeys {
		const val TOKEN = "user.token"
		const val LANGUAGE = "user.language"
		const val SEEDER = "seeder"
	}
}