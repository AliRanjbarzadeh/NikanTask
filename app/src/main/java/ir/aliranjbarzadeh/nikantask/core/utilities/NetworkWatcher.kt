package ir.aliranjbarzadeh.nikantask.core.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ir.aliranjbarzadeh.nikantask.core.dispatchers.DispatchersProvider
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkWatcher @Inject constructor(private val context: Context, private val dispatchersProvider: DispatchersProvider) {
	private fun isDeviceConnected(): Boolean {
		val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

		val network = connectivityManager.activeNetwork ?: return false
		val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
		return when {
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
			activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
			else -> false
		}
	}

	suspend fun isInternetAvailable(): Boolean {
		if (!isDeviceConnected()) {
			return false
		}

		return withContext(dispatchersProvider.getIO()) {
			try {
				// Connect to Google DNS to check for internet access
				val socket = Socket()
				val socketAddress = InetSocketAddress("8.8.8.8", 53)
				socket.connect(socketAddress, 1500)
				socket.close()
				true
			} catch (e: IOException) {
				false
			}
		}
	}
}