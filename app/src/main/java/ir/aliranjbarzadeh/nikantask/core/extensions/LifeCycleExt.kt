package ir.aliranjbarzadeh.nikantask.core.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.observe(flow: StateFlow<T>, observer: (t: T) -> Unit) {
	lifecycleScope.launch {
		flow.collect { observer(it) }
	}
}

fun <T> LifecycleOwner.repeatObserve(flow: StateFlow<T>, observer: (t: T) -> Unit) {
	lifecycleScope.launch {
		repeatOnLifecycle(Lifecycle.State.STARTED) {
			flow.collectLatest { observer(it) }
		}
	}
}