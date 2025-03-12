package ir.aliranjbarzadeh.nikantask.core.exceptions

import com.google.gson.Gson
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.domain.StatusCode
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton
import ir.aliranjbarzadeh.nikantask.data.models.Error as BaseError


@Singleton
class NetworkExceptionHandler @Inject constructor(private val gson: Gson, private val logger: Logger) {
	fun traceErrorException(throwable: Throwable?): BaseError {
		logger.debug(throwable?.message, "HTTP_EXP")
		val error: BaseError = when (throwable) {

			// if throwable is an instance of HttpException
			// then attempt to parse error data from response body
			is HttpException -> {
				logger.debug(throwable.code(), "HTTP_EXP")
				getHttpError(throwable.code(), throwable.response()?.errorBody())
			}

			// handle api call timeout error
			is SocketTimeoutException -> {
				BaseError(
					message = "زمان پاسخ سرور پایان یافته، لطفا دوباره تلاش کنید",
					statusCode = StatusCode.TimeOut
				)
			}

			// handle connection error
			is IOException -> {
				BaseError(
					statusCode = StatusCode.IOException,
					message = "خطایی در اتصال به سرور رخ داده است، لطفا دوباره تلاش کنید",
				)
			}

			else -> BaseError(
				message = "خطایی نامشخص، لطفا دوباره تلاش کنید"
			)
		}
		return error
	}

	/**
	 * attempts to parse http response body and get error data from it
	 *
	 * @param body retrofit response body
	 * @return returns an instance of [BaseError] with parsed data or NOT_DEFINED status
	 */
	private fun getHttpError(statusCode: Int, body: ResponseBody?): BaseError {
		return try {
			val error = gson.fromJson(body?.string(), BaseError::class.java)
			error.statusCode = StatusCode.fromValue(statusCode)
			error
		} catch (e: Throwable) {
			e.printStackTrace()
			BaseError(message = e.message.toString())
		}

	}
}