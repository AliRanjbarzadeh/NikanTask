package ir.aliranjbarzadeh.nikantask.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(private val dispatchers: DispatchersProvider) : ViewModel(), CoroutineScope {
	var isFirstRun: Boolean = true
	open protected val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
	open protected val _store: MutableLiveData<Long> = MutableLiveData()
	open protected val _update: MutableLiveData<Int> = MutableLiveData()
	open protected val _isEmptyList: MutableLiveData<Boolean> = MutableLiveData()
	protected val _error: MutableLiveData<Int> = MutableLiveData()

	override val coroutineContext: CoroutineContext
		get() = dispatchers.getMain() + SupervisorJob()

	fun execute(job: suspend () -> Unit) = launch {
		withContext(dispatchers.getIO()) { job.invoke() }
	}

	open fun isLoading(): MutableLiveData<Boolean> = _isLoading

	fun store(): MutableLiveData<Long> = _store

	fun update(): MutableLiveData<Int> = _update

	open fun isEmptyList(): MutableLiveData<Boolean> = _isEmptyList

	fun error(): MutableLiveData<Int> = _error

}