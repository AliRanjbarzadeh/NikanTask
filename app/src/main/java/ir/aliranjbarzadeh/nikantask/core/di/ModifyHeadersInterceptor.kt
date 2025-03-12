package ir.aliranjbarzadeh.nikantask.core.di

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class ModifyHeadersInterceptor @Inject constructor() : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val builder = request.newBuilder()

		try {
			builder.header("Cache-Control", "no-cache")
			builder.header("Content-Type", "application/json")
		} catch (_: Exception) {
		}

		val builderResponse = chain.proceed(builder.build())
		return builderResponse
	}
}