package ir.aliranjbarzadeh.nikantask.presentation

import android.graphics.Typeface
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.BaseActivity
import ir.aliranjbarzadeh.nikantask.core.extensions.changeFont
import ir.aliranjbarzadeh.nikantask.core.helpers.FontHelper
import ir.aliranjbarzadeh.nikantask.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

	private lateinit var navController: NavController

	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()
		super.onCreate(savedInstanceState)

		//bottom navigation
		setupBottomNavigation()
	}

	private fun setupBottomNavigation() {
		//Set font
		FontHelper.getTypeFace(this)?.also { typeface: Typeface ->
			binding.bottomNavigation.changeFont(typeface)
		}

		//Find navHost
		val navHostFragment = supportFragmentManager.findFragmentById(R.id.base_nav_host) as NavHostFragment

		//Set nav controller to access in the MainActivity
		navController = navHostFragment.navController

		//Setup bottom navigation with navController
		binding.bottomNavigation.setupWithNavController(navController)

		//Handle reselect item to back to startDestination of bottom nav item
		binding.bottomNavigation.setOnItemReselectedListener { item ->
			val currentDestinationId = navController.currentDestination?.id
			if (currentDestinationId == null) {
				return@setOnItemReselectedListener
			}
			when (item.itemId) {
				R.id.orders_nav -> {
				}

				R.id.products_nav -> {
				}

				R.id.customers_nav -> {
				}
			}
		}
	}
}