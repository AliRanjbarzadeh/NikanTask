package ir.aliranjbarzadeh.nikantask.data.sources.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.TokenModel
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.UserModel

@Dao
interface AuthDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveToken(tokenModel: TokenModel): Long

	@Query("SELECT * FROM users LIMIT 1")
	suspend fun getUser(): UserModel?

	@Query("DELETE FROM tokens WHERE token = :token")
	suspend fun logout(token: String): Int

	@Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
	suspend fun authenticate(username: String, password: String): UserModel?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun saveUser(userModel: UserModel): Long
}