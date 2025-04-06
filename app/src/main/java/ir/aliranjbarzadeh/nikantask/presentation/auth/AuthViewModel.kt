package ir.aliranjbarzadeh.nikantask.presentation.auth

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.aliranjbarzadeh.nikantask.core.BaseViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.core.interfaces.StoreViewModel
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.usecases.auth.AuthenticateUseCase
import ir.aliranjbarzadeh.nikantask.domain.usecases.auth.InitUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
	val dispatchersProvider: DispatchersProvider,
	private val initUserUseCase: InitUserUseCase,
	private val authenticateUseCase: AuthenticateUseCase,
) : BaseViewModel(dispatchersProvider), StoreViewModel<Boolean> {

	override val _store = MutableStateFlow<ResponseResult.Success<Boolean>?>(null)
	override val store: StateFlow<ResponseResult.Success<Boolean>?> = _store.asStateFlow()

	private val _auth = MutableStateFlow<ResponseResult.Success<String?>?>(null)
	val auth: StateFlow<ResponseResult.Success<String?>?> = _auth.asStateFlow()

	val username = MutableLiveData<String>("")
	val password = MutableLiveData<String>("")

	fun saveUser() {
		executeWithLoading {
			initUserUseCase().collectLatest { result ->
				if (result is ResponseResult.Success) {
					_store.value = result
				} else if (result is ResponseResult.Error) {
					setError(result)
				}
			}
		}
	}

	fun login() {
		executeWithLoading {
			authenticateUseCase(username.value!!, password.value!!).collectLatest { result ->
				if (result is ResponseResult.Success) {
					_auth.value = result
				} else if (result is ResponseResult.Error) {
					setError(result)
				}
			}
		}
	}
}