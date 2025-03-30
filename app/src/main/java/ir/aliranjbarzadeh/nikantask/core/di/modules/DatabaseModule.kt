package ir.aliranjbarzadeh.nikantask.core.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.aliranjbarzadeh.nikantask.core.AppConfig
import ir.aliranjbarzadeh.nikantask.core.helpers.PackageHelper
import ir.aliranjbarzadeh.nikantask.core.utilities.Logger
import ir.aliranjbarzadeh.nikantask.data.sources.local.Database
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.CustomerDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.OrderDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.ProductDao
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
	@Provides
	@Singleton
	fun providesDatabase(@ApplicationContext appContext: Context, logger: Logger): Database = Room.databaseBuilder(
		appContext, Database::class.java, AppConfig.DATABASE
	).apply {
		if (PackageHelper.isDebuggable(appContext)) {
			fallbackToDestructiveMigration()
		} else {
			//Put migrations here
		}

		setQueryCallback({ sqlQuery, bindArgs ->
			// Log the query and its arguments
			logger.info("SQL Query: $sqlQuery, Args: $bindArgs", "RoomQuery")
		}, Executors.newSingleThreadExecutor())
	}.build()

	/*===================Daos===================*/
	@Provides
	@Singleton
	fun providesCustomerDao(database: Database): CustomerDao = database.customerDao

	@Provides
	@Singleton
	fun providesProductDao(database: Database): ProductDao = database.productDao

	@Provides
	@Singleton
	fun providesOrderDao(database: Database): OrderDao = database.orderDao


}