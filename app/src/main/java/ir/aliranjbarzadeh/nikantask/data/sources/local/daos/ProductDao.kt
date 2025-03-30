package ir.aliranjbarzadeh.nikantask.data.sources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.ProductModel

@Dao
interface ProductDao {
	@Query("SELECT * FROM products WHERE deletedAt IS NULL ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
	suspend fun list(limit: Int, offset: Int): List<ProductModel>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun store(productModel: ProductModel): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun storeMany(products: List<ProductModel>): List<Long>

	@Update(onConflict = OnConflictStrategy.IGNORE)
	suspend fun update(productModel: ProductModel): Int

	@Query("UPDATE products SET deletedAt = CURRENT_TIMESTAMP WHERE productId = :id")
	suspend fun destroy(id: Long): Int

	@Query("DELETE FROM products")
	suspend fun deleteAll()

	@Query("DELETE FROM sqlite_sequence WHERE name = 'products'")
	suspend fun resetAutoIncrement()

	@Transaction
	suspend fun truncate() {
		deleteAll()
		resetAutoIncrement()
	}
}