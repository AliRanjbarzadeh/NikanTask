package ir.aliranjbarzadeh.nikantask.data.models

import com.google.gson.JsonElement
import ir.aliranjbarzadeh.nikantask.domain.network.HttpErrors


data class Error(
	val message: String? = "",
	val data: JsonElement? = null,
	val status: Boolean? = false,
	var statusCode: HttpErrors = HttpErrors.NotDefined,
) {
	override fun toString(): String {
		return "ErrorBody(message=$message, data=$data, status=$status, statusCode=$statusCode)"
	}
}