package ir.aliranjbarzadeh.nikantask.core.utilities

interface Logger {
	fun error(msg: Any?, tag: String?)
	fun debug(msg: Any?, tag: String?)
	fun info(msg: Any?, tag: String?)
}