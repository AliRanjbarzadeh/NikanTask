package ir.aliranjbarzadeh.nikantask.data.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.aliranjbarzadeh.nikantask.core.AppConfig
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.CustomerDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.OrderDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.ProductDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.CustomerModel
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.OrderModel
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.OrderProductPivot
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.ProductModel
import ir.aliranjbarzadeh.nikantask.data.sources.local.Database as BaseDB

@Database(
	entities = [CustomerModel::class, ProductModel::class, OrderModel::class, OrderProductPivot::class],
	version = 1,
	exportSchema = true
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
	abstract val customerDao: CustomerDao
	abstract val productDao: ProductDao
	abstract val orderDao: OrderDao

	companion object {
		@Volatile
		private var INSTANCE: BaseDB? = null

		fun getDatabase(context: Context): BaseDB {
			return INSTANCE ?: synchronized(this) {
				val instance = Room.databaseBuilder(
					context,
					BaseDB::class.java,
					AppConfig.DATABASE
				).build()

				INSTANCE = instance
				instance
			}
		}
	}
}