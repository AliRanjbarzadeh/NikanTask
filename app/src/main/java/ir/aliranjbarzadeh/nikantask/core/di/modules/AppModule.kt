package ir.aliranjbarzadeh.nikantask.core.di.modules

import android.content.ContentResolver
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProviderImpl
import ir.aliranjbarzadeh.nikantask.core.exceptions.LocalExceptionHandler
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Provides
	@Singleton
	fun providesContentResolver(@ApplicationContext appContext: Context): ContentResolver = appContext.contentResolver

	@Provides
	@Singleton
	fun providesGson(): Gson = GsonBuilder().create()

	@Provides
	@Singleton
	fun providesDispatcher(dispatcherProvider: DispatchersProviderImpl): DispatchersProvider = dispatcherProvider.dispatcher

	@Provides
	@Singleton
	fun providesLogger(@ApplicationContext appContext: Context): Logger = Logger(appContext)

	@Provides
	@Singleton
	fun providesLocalExceptionHandler(): LocalExceptionHandler = LocalExceptionHandler()
}