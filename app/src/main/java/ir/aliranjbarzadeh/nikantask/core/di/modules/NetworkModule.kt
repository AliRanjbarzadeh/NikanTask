package ir.aliranjbarzadeh.nikantask.core.di.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.aliranjbarzadeh.nikantask.core.di.BaseHttpClient
import ir.aliranjbarzadeh.nikantask.core.di.BaseNetwork
import ir.aliranjbarzadeh.nikantask.core.di.BaseRetrofit
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.exceptions.NetworkExceptionHandler
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.core.utilities.NetworkWatcher
import ir.aliranjbarzadeh.nikantask.data.api.ApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
	@Provides
	@Singleton
	fun providesNetworkWatcher(@ApplicationContext context: Context, dispatchersProvider: DispatchersProvider): NetworkWatcher = NetworkWatcher(context, dispatchersProvider)

	@Provides
	@Singleton
	fun providesOkHttpClient(baseHttpClient: BaseHttpClient): OkHttpClient = baseHttpClient.okHttpClient

	@Provides
	@Singleton
	fun providesApiExceptionHandler(gson: Gson, logger: Logger): NetworkExceptionHandler = NetworkExceptionHandler(gson, logger)

	@Provides
	@Singleton
	fun providesCache(@ApplicationContext appContext: Context): Cache = Cache(appContext.cacheDir, 30 * 1024 * 1024)

	@Provides
	@Singleton
	fun providesBaseUrl(@ApplicationContext appContext: Context): BaseNetwork = BaseNetwork(appContext)

	@Provides
	@Singleton
	fun providesRetrofit(baseRetrofit: BaseRetrofit): Retrofit = baseRetrofit.retrofit

	@Provides
	@Singleton
	fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}