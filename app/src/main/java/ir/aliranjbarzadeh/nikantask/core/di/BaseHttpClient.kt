package ir.aliranjbarzadeh.nikantask.core.di

import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class BaseHttpClient @Inject constructor(
    cache: Cache,
    modifyHeadersInterceptor: ModifyHeadersInterceptor
) {
    val okHttpClient = OkHttpClient()
        .newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addNetworkInterceptor(modifyHeadersInterceptor)
        .build()
}