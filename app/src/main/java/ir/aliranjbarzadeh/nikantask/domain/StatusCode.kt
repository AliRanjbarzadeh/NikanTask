package ir.aliranjbarzadeh.nikantask.domain

import ir.aliranjbarzadeh.nikantask.domain.StatusCode.entries


enum class StatusCode(val value: Int) {
	NotDefined(0), // Default or unknown error
	IOException(1),
	Unauthorized(401),
	ServerError(500),
	TimeOut(408),
	BadRequest(400),
	Forbidden(403),
	BadResponse(502),
	Conflict(409),
	RoomList(-1),
	RoomStore(-2),
	RoomUpdate(-3),
	RoomDestroy(-4),
	RoomSeed(-5),
	RoomOrderProducts(-6),
	RoomAuth(-7);

	companion object {
		fun fromValue(value: Int?): StatusCode {
			return entries.find { it.value == value } ?: NotDefined
		}
	}
}