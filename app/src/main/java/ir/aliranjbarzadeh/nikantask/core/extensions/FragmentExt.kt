package ir.aliranjbarzadeh.nikantask.core.extensions

import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

fun Fragment.navTo(action: NavDirections) {
	findNavController().navigate(action)
}

fun Fragment.navTo(deepLink: String) {
	val request = NavDeepLinkRequest.Builder
		.fromUri(deepLink.toUri())
		.build()
	findNavController().navigate(request)
}