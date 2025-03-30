package ir.aliranjbarzadeh.nikantask.data.sources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.OrderModel
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.OrderProductPivot
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.OrderSummeryModel
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.ProductWithQuantityModel

@Dao
interface OrderDao {
	@Query(
		"""
		SELECT
		o.orderId AS order_orderId,
		o.customerRefId AS order_customerRefId,
		o.createdAt AS order_createdAt,
		o.updatedAt AS order_updatedAt,
		o.deletedAt AS order_deletedAt,
		c.customerId AS customer_customerId,
		c.name AS customer_name,
		c.mobile AS customer_mobile,
		c.createdAt AS customer_createdAt,
		c.updatedAt AS customer_updatedAt,
		c.deletedAt AS customer_deletedAt,
		COUNT(op.productRefId) AS totalItems
		FROM orders o
		INNER JOIN order_products op ON o.orderId = op.orderRefId
		INNER JOIN customers c ON o.customerRefId = c.customerId
		WHERE o.deletedAt IS NULL
		GROUP BY o.orderId
		ORDER BY o.createdAt DESC, o.orderId DESC
		LIMIT :limit OFFSET :offset
	"""
	)
	suspend fun list(limit: Int, offset: Int): List<OrderSummeryModel>

	@Query(
		"""
		SELECT
		p.productId,
		p.name,
		p.code,
		p.createdAt,
		p.updatedAt,
		p.deletedAt,
		op.quantity
		FROM products p
		INNER JOIN order_products op ON p.productId = op.productRefId
		WHERE op.orderRefId = :orderId
	"""
	)
	suspend fun orderProducts(orderId: Long): List<ProductWithQuantityModel>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun store(orderModel: OrderModel): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun storeOrderProductPivots(pivots: List<OrderProductPivot>)

	@Transaction
	suspend fun storeOrderWithProducts(orderModel: OrderModel, pivots: List<OrderProductPivot>): Long {
		val orderId = store(orderModel)

		storeOrderProductPivots(pivots.map { it.copy(orderRefId = orderId) })

		return orderId
	}

	@Update(onConflict = OnConflictStrategy.IGNORE)
	suspend fun update(orderModel: OrderModel): Int

	@Query("DELETE FROM order_products WHERE orderRefId = :orderId")
	suspend fun deleteOrderProductPivots(orderId: Long)

	@Transaction
	suspend fun updateOrderWithProducts(orderModel: OrderModel, pivots: List<OrderProductPivot>) {
		update(orderModel)

		deleteOrderProductPivots(orderModel.orderId)

		storeOrderProductPivots(pivots.map { it.copy(orderRefId = orderModel.orderId) })
	}

	@Query("UPDATE orders SET deletedAt = CURRENT_TIMESTAMP WHERE orderId = :id")
	suspend fun destroy(id: Long): Int

	@Query("DELETE FROM order_products")
	suspend fun deleteAllPivots()

	@Query("DELETE FROM sqlite_sequence WHERE name = 'order_products'")
	suspend fun resetAutoIncrementPivot()

	@Query("DELETE FROM orders")
	suspend fun deleteAll()

	@Query("DELETE FROM sqlite_sequence WHERE name = 'orders'")
	suspend fun resetAutoIncrement()

	@Transaction
	suspend fun truncate() {
		deleteAllPivots()
		resetAutoIncrementPivot()

		deleteAll()
		resetAutoIncrement()
	}
}