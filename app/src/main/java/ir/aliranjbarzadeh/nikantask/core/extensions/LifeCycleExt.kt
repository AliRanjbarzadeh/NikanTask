package ir.aliranjbarzadeh.nikantask.core.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.observe(flow: Flow<T>, observer: (t: T) -> Unit) {
	lifecycleScope.launch {
		repeatOnLifecycle(Lifecycle.State.STARTED) {
			flow.collect { observer(it) }
		}
	}
}