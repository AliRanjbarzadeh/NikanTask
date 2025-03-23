package ir.aliranjbarzadeh.nikantask.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(private val dispatchers: DispatchersProvider) : ViewModel(), CoroutineScope {
	protected open val _isLoading = MutableStateFlow<Boolean?>(null)
	val isLoading = _isLoading.asStateFlow()

	protected val _error = MutableStateFlow<ResponseResult.Error?>(null)
	val error = _error.asStateFlow()


	override val coroutineContext: CoroutineContext
		get() = dispatchers.getMain() + SupervisorJob()

	fun setError(result: ResponseResult.Error) {
		_error.value = result
	}

	fun execute(job: suspend () -> Unit) {
		viewModelScope.launch {
			withContext(dispatchers.getIO()) {
				job.invoke()
			}
		}
	}

	fun executeWithLoading(job: suspend () -> Unit) {
		viewModelScope.launch {
			withContext(dispatchers.getIO()) {
				_isLoading.value = true
				job.invoke()
				_isLoading.value = false
			}
		}
	}
}