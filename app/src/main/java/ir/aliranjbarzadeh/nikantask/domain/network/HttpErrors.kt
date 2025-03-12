package ir.aliranjbarzadeh.nikantask.domain.network


enum class HttpErrors(val value: Int) {
	Unauthorized(401),
	ServerError(500),
	TimeOut(408),
	BadRequest(400),
	Forbidden(403),
	BadResponse(502),
	NotDefined(0),    // Default or unknown error
	Conflict(409);

	companion object {
		fun fromValue(value: Int?): HttpErrors {
			return entries.find { it.value == value } ?: NotDefined
		}
	}
}