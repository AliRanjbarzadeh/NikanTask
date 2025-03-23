package ir.aliranjbarzadeh.nikantask.data.sources.local.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.CustomerModel

@Dao
interface CustomerDao {
	@Query("SELECT * FROM customers WHERE deleted_at IS NULL ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
	suspend fun list(limit: Int, offset: Int): List<CustomerModel>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun store(customerModel: CustomerModel): Long

	@Update(onConflict = OnConflictStrategy.IGNORE)
	suspend fun update(customerModel: CustomerModel): Int

	@Query("UPDATE customers SET deleted_at = CURRENT_TIMESTAMP WHERE id = :id")
	suspend fun destroy(id: Long): Int
}