package ir.aliranjbarzadeh.nikantask.presentation.libs

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.get
import androidx.core.view.size
import androidx.databinding.Observable
import androidx.databinding.ObservableInt
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textview.MaterialTextView

class StackedBottomNavigationView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : BottomNavigationView(context, attrs) {

	private val navControllers = mutableMapOf<Int, NavController>()
	private var activeNavController: NavController? = null

	fun changeFont(typeface: Typeface) {
		for (i in 0 until menu.size) {
			val menuItemView = findViewById<View?>(menu[i].itemId)

			val menuLabelSmall = menuItemView?.findViewById<MaterialTextView?>(R.id.navigation_bar_item_small_label_view)
			menuLabelSmall?.typeface = typeface

			val menuLabelLarge = menuItemView?.findViewById<MaterialTextView?>(R.id.navigation_bar_item_large_label_view)
			menuLabelLarge?.typeface = typeface
		}
	}

	fun setBadge(
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

	fun getCurrentNavController(): NavController? {
		return activeNavController
	}

	fun setupWithNavControllerStacked(
		fragmentManager: FragmentManager,
		containerId: Int,
		navGraphMap: Map<Int, Int>,
		listener: (Int) -> Unit,
	) {
		setOnItemSelectedListener { item ->
			switchTab(fragmentManager, containerId, item.itemId, navGraphMap, listener)
			true
		}

		// Set default tab
		if (navGraphMap.isNotEmpty()) {
			val defaultTabId = navGraphMap.keys.first()
			post { switchTab(fragmentManager, containerId, defaultTabId, navGraphMap, listener) }
		}
	}

	fun handleBackPress(defaultTabId: Int): Boolean {
		val currentNavController = activeNavController ?: return false
		val currentFragmentId = currentNavController.currentDestination?.id
		val startDestinationId = currentNavController.graph.startDestinationId

		return if (currentFragmentId != startDestinationId) {// If current tab has back stack
			currentNavController.popBackStack()
			true
		} else if (selectedItemId != defaultTabId) {
			// If no back stack go to first tab
			selectedItemId = defaultTabId
			true
		} else { // Handle back with activity
			false
		}
	}

	fun setupOnItemReselect(listener: (NavController) -> Unit) {
		setOnItemReselectedListener { item ->
			navControllers[item.itemId]?.let { listener(it) }
		}
	}

	private fun switchTab(
		fragmentManager: FragmentManager,
		containerId: Int,
		tabId: Int,
		navGraphMap: Map<Int, Int>,
		listener: (Int) -> Unit,
	) {
		val existingNavController = navControllers[tabId]

		val transaction = fragmentManager.beginTransaction()

		// Detach currently active fragment if it exists
		activeNavController?.let { currentNavController ->
			fragmentManager.findFragmentByTag(currentNavController.graph.id.toString())?.let {
				transaction.detach(it)
			}
		}

		if (existingNavController == null) {
			// First time switching to this tab -> Create and add NavHostFragment
			val navGraphId = navGraphMap[tabId] ?: return
			val navHostFragment = NavHostFragment.create(navGraphId)
			fragmentManager.beginTransaction()
				.add(containerId, navHostFragment, tabId.toString()) // ✅ Lazily commit the fragment
				.commitNow()

			navControllers[tabId] = navHostFragment.navController
			activeNavController = navHostFragment.navController
		} else {
			// If the fragment exists, just attach it
			fragmentManager.findFragmentByTag(tabId.toString())?.let {
				transaction.attach(it)
			}
			activeNavController = existingNavController
		}

		transaction.commit()

		//Call for change title
		listener(tabId)
	}
}