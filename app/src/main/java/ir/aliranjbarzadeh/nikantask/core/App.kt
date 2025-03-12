package ir.aliranjbarzadeh.nikantask.core

import androidx.multidex.MultiDexApplication
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp
import ir.aliranjbarzadeh.nikantask.core.helpers.FontHelper
import ir.aliranjbarzadeh.nikantask.core.helpers.LanguageHelper
import ir.aliranjbarzadeh.nikantask.core.helpers.LocaleHelper

@HiltAndroidApp
class App : MultiDexApplication() {
	override fun onCreate() {
		super.onCreate()
		initHawk()
		initLanguage()
		initFont()
	}

	//Hawk is a 3D party library to use secure shared preference
	private fun initHawk() {
		Hawk.init(this).build()
	}

	//Set language for whole of app
	private fun initLanguage() {
		LocaleHelper.setLocale(this, LanguageHelper.getLanguage())
	}

	//Set font of app
	private fun initFont() {
		FontHelper.setFont(this)
	}
}