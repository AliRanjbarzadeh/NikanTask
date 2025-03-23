package ir.aliranjbarzadeh.nikantask.data.sources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.ProductModel

@Dao
interface ProductDao {
	@Query("SELECT * FROM products WHERE deleted_at IS NULL ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
	suspend fun list(limit: Int, offset: Int): List<ProductModel>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun store(productModel: ProductModel): Long

	@Update(onConflict = OnConflictStrategy.IGNORE)
	suspend fun update(productModel: ProductModel): Int

	@Query("UPDATE products SET deleted_at = CURRENT_TIMESTAMP WHERE id = :id")
	suspend fun destroy(id: Long): Int
}