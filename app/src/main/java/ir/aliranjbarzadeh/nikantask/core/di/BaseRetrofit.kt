package ir.aliranjbarzadeh.nikantask.core.di

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BaseRetrofit @Inject constructor(okHttpClient: OkHttpClient, gson: Gson, baseNetwork: BaseNetwork) {
	val retrofit: Retrofit = Retrofit.Builder()
		.baseUrl(baseNetwork.baseUrl)
		.addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
		.addConverterFactory(GsonConverterFactory.create(gson))
		.client(okHttpClient)
		.build()
}