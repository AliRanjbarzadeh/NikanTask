package ir.aliranjbarzadeh.nikantask.data.sources.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserModel(
	@PrimaryKey(autoGenerate = true)
	val userId: Long = 0,
	val username: String,
	val password: String,
)
