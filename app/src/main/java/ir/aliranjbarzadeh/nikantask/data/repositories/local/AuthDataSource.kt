package ir.aliranjbarzadeh.nikantask.data.repositories.local

import ir.aliranjbarzadeh.nikantask.core.exceptions.LocalExceptionHandler
import ir.aliranjbarzadeh.nikantask.core.helpers.DateTimeHelper
import ir.aliranjbarzadeh.nikantask.data.repositories.AuthDataSource
import ir.aliranjbarzadeh.nikantask.data.sources.local.daos.AuthDao
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.TokenModel
import ir.aliranjbarzadeh.nikantask.data.sources.local.models.UserModel
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.StatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(
	private val dao: AuthDao,
	private val localExceptionHandler: LocalExceptionHandler,
) : AuthDataSource {
	override fun initUser(): Flow<ResponseResult<Boolean>> = flow {
		try {
			var userModel = dao.getUser()
			if (userModel != null) {
				emit(ResponseResult.Success(true))
			} else {
				userModel = UserModel(username = "demo", password = "demo")
				emit(ResponseResult.Success(dao.saveUser(userModel) > 0))
			}
		} catch (e: Exception) {
			emit(
				ResponseResult.Error(
					localExceptionHandler.traceErrorException(
						throwable = e,
						statusCode = StatusCode.RoomStore
					)
				)
			)
		}
	}

	override fun login(username: String, password: String): Flow<ResponseResult<String?>> = flow {
		try {
			dao.authenticate(username, password)?.let { user ->
				val tokenModel = TokenModel(
					userRefId = user.userId,
					token = generateFakeToken(user.userId),
					createdAt = DateTimeHelper.currentDateUTC()
				)
				if (dao.saveToken(tokenModel) > 0) {
					emit(ResponseResult.Success(tokenModel.token))
				} else {
					throw Exception("مشکلی پیش آمده لطفا دوباره تلاش کنید")
				}
			} ?: run { throw Exception("نام کاربری و یا کلمه عبور اشتباه می باشد") }
		} catch (e: Exception) {
			emit(
				ResponseResult.Error(
					localExceptionHandler.traceErrorException(
						throwable = e,
						statusCode = StatusCode.RoomAuth
					)
				)
			)
		}
	}

	override fun logout(token: String): Flow<ResponseResult<Boolean>> = flow {
		try {
			emit(ResponseResult.Success(dao.logout(token) > 0))
		} catch (e: Exception) {
			emit(
				ResponseResult.Error(
					localExceptionHandler.traceErrorException(
						throwable = e,
						statusCode = StatusCode.RoomAuth
					)
				)
			)
		}
	}

	private fun generateFakeToken(userId: Long): String {
		return "fakeTokenGenerated$userId"
	}
}