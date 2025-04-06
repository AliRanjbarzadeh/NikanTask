package ir.aliranjbarzadeh.nikantask.data.sources.local.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
	tableName = "tokens",
	foreignKeys = [
		ForeignKey(
			entity = UserModel::class,
			parentColumns = ["userId"],
			childColumns = ["userRefId"],
			onDelete = ForeignKey.CASCADE
		)
	],
	indices = [Index("userRefId")]
)
data class TokenModel(
	@PrimaryKey(autoGenerate = true)
	val tokenId: Long = 0,
	val userRefId: Long,
	val token: String,
	val createdAt: Date,
)
