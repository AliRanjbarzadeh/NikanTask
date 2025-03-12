package ir.aliranjbarzadeh.nikantask.core.extensions

import com.orhanobut.hawk.Hawk

fun <T> saveToSp(key: String, t: T) = Hawk.put(key, t)

fun <T> loadFromSp(key: String): T = Hawk.get(key)

fun <T> loadFromSp(key: String, defaultValue: T): T {
	return if (existInSp(key))
		Hawk.get(key)
	else
		defaultValue
}

fun existInSp(key: String): Boolean = Hawk.contains(key)

fun deleteAllInSp() = Hawk.deleteAll()

fun deleteFromSp(key: String) = Hawk.delete(key)