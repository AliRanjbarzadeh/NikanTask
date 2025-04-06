package ir.aliranjbarzadeh.nikantask.data.sources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.CustomerModel

@Dao
interface CustomerDao {
	@Query("SELECT * FROM customers WHERE deletedAt IS NULL ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
	suspend fun list(limit: Int, offset: Int): List<CustomerModel>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun store(customerModel: CustomerModel): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun storeMany(customers: List<CustomerModel>): List<Long>

	@Update(onConflict = OnConflictStrategy.IGNORE)
	suspend fun update(customerModel: CustomerModel): Int

	@Query("UPDATE customers SET deletedAt = CURRENT_TIMESTAMP WHERE customerId = :id")
	suspend fun destroy(id: Long): Int

	@Query("DELETE FROM customers")
	suspend fun deleteAll()

	@Query("DELETE FROM sqlite_sequence WHERE name = 'customers'")
	suspend fun resetAutoIncrement()

	@Transaction
	suspend fun truncate() {
		deleteAll()
		resetAutoIncrement()
	}
}