package ir.aliranjbarzadeh.nikantask.presentation

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseActivity
import ir.aliranjbarzadeh.nikantask.core.helpers.FontHelper
import ir.aliranjbarzadeh.nikantask.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setupBottomNavigation()

		onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				if (!binding.bottomNavigation.handleBackPress(R.id.orders_nav)) {
					finish()
				}
			}
		})
	}

	override fun onSupportNavigateUp(): Boolean {
		return binding.bottomNavigation.getCurrentNavController()?.navigateUp() ?: false
	}

	@SuppressLint("RestrictedApi")
	private fun setupBottomNavigation() {
		//Set font
		FontHelper.getTypeFace(this)?.also { typeface: Typeface ->
			binding.bottomNavigation.changeFont(typeface)
		}


		//Setup bottom navigation with navController
		binding.bottomNavigation.setupWithNavControllerStacked(
			supportFragmentManager,
			R.id.base_nav_host,
			mapOf(
				R.id.orders_nav to R.navigation.orders_nav,
				R.id.products_nav to R.navigation.products_nav,
				R.id.customers_nav to R.navigation.customers_nav
			)
		) { tabId ->
			when (tabId) {
				R.id.orders_nav -> setTitle(R.string.orders)
				R.id.products_nav -> setTitle(R.string.products)
				R.id.customers_nav -> setTitle(R.string.customers)
			}
		}

		//Setup on item reselect listener
		binding.bottomNavigation.setupOnItemReselect { navController ->
			navController.popBackStack(navController.graph.startDestinationId, false)
		}
	}
}