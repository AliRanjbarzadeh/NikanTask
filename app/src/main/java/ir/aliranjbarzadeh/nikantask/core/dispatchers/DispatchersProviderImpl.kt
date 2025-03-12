package ir.aliranjbarzadeh.nikantask.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DispatchersProviderImpl @Inject constructor() {
	val dispatcher = object : DispatchersProvider {
		override fun getIO(): CoroutineDispatcher {
			return Dispatchers.IO
		}

		override fun getMain(): CoroutineDispatcher {
			return Dispatchers.Main
		}

		override fun getDefault(): CoroutineDispatcher {
			return Dispatchers.Default
		}
	}

}