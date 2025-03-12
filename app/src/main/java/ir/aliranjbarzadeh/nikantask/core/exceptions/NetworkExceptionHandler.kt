package ir.aliranjbarzadeh.nikantask.core.exceptions

import com.google.gson.Gson
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.models.Error
import ir.aliranjbarzadeh.nikantask.domain.network.HttpErrors
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkExceptionHandler @Inject constructor(private val gson: Gson, private val logger: Logger) {
	fun traceErrorException(throwable: Throwable?): Error {
		logger.debug(throwable?.message, "HTTP_EXP")
		val error: Error = when (throwable) {

			// if throwable is an instance of HttpException
			// then attempt to parse error data from response body
			is HttpException -> {
				logger.debug(throwable.code(), "HTTP_EXP")
				getHttpError(throwable.code(), throwable.response()?.errorBody())
			}

			// handle api call timeout error
			is SocketTimeoutException -> {
				Error(
					status = false,
					message = "زمان پاسخ سرور پایان یافته، لطفا دوباره تلاش کنید"
				)
			}

			// handle connection error
			is IOException -> {
				Error(
					status = false,
					message = "خطایی در اتصال به سرور رخ داده است، لطفا دوباره تلاش کنید",
				)
			}

			else -> Error(
				status = false,
				message = "خطایی نامشخص، لطفا دوباره تلاش کنید"
			)
		}
		return error
	}

	/**
	 * attempts to parse http response body and get error data from it
	 *
	 * @param body retrofit response body
	 * @return returns an instance of [Error] with parsed data or NOT_DEFINED status
	 */
	private fun getHttpError(statusCode: Int, body: ResponseBody?): Error {
		return try {
			val error = gson.fromJson(body?.string(), Error::class.java)
			error.statusCode = HttpErrors.fromValue(statusCode)
			error
		} catch (e: Throwable) {
			e.printStackTrace()
			Error(status = false, message = e.message.toString())
		}

	}
}