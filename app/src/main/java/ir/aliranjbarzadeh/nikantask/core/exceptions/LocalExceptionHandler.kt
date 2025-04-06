package ir.aliranjbarzadeh.nikantask.core.exceptions

import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import ir.aliranjbarzadeh.nikantask.domain.StatusCode
import javax.inject.Singleton
import ir.aliranjbarzadeh.nikantask.data.models.Error as BaseError

@Singleton
class LocalExceptionHandler {
	fun traceErrorException(throwable: Throwable?, statusCode: StatusCode): BaseError {
		return try {
			BaseError(
				message = throwable?.message ?: "",
				statusCode = statusCode,
				data = throwable?.stackTraceToString(),
				date = DateTimeHelper.currentDate().time
			)
		} catch (e: Exception) {
			BaseError(
				message = e.message.toString(),
				statusCode = statusCode,
				date = DateTimeHelper.currentDate().time
			)
		}
	}
}