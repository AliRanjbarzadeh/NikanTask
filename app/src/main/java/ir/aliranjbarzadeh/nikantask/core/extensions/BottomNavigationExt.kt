package ir.aliranjbarzadeh.nikantask.core.extensions

import android.graphics.Typeface
import android.view.View
import androidx.annotation.IdRes
import androidx.databinding.Observable
import androidx.databinding.ObservableInt
import com.google.android.material.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textview.MaterialTextView
import kotlin.let
import kotlin.ranges.until
import androidx.core.view.size
import androidx.core.view.get

fun BottomNavigationView.changeFont(typeface: Typeface) {
	for (i in 0 until menu.size) {
		val menuItemView =
			findViewById<View?>(menu[i].itemId)

		val menuLabelSmall =
			menuItemView?.findViewById<MaterialTextView?>(R.id.navigation_bar_item_small_label_view)
		menuLabelSmall?.typeface = typeface

		val menuLabelLarge =
			menuItemView?.findViewById<MaterialTextView?>(R.id.navigation_bar_item_large_label_view)
		menuLabelLarge?.typeface = typeface
	}
}

fun BottomNavigationView.setBadge(
	@IdRes
	tabResId: Int,
	badgeValue: ObservableInt,
) {
	getOrCreateBadge(tabResId).let { badge ->
		badge.isVisible = badgeValue.get() > 0
		badge.number = badgeValue.get()
		badgeValue.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
			override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
				badge.isVisible = badgeValue.get() > 0
				badge.number = if (badgeValue.get() > 99) {
					99
				} else {
					badgeValue.get()
				}
			}
		})
	}
}