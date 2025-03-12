package ir.aliranjbarzadeh.nikantask.data.models

import com.google.gson.JsonElement
import ir.aliranjbarzadeh.nikantask.domain.StatusCode


data class Error(
	val message: String? = "",
	val data: JsonElement? = null,
	var statusCode: StatusCode = StatusCode.NotDefined,
) {
	override fun toString(): String {
		return "ErrorBody(message=$message, data=$data, statusCode=$statusCode)"
	}
}