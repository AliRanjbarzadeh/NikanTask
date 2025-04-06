package ir.aliranjbarzadeh.nikantask.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import ir.aliranjbarzadeh.nikantask.R
import ir.aliranjbarzadeh.nikantask.core.AppConfig
import ir.aliranjbarzadeh.nikantask.core.BaseActivity
import ir.aliranjbarzadeh.nikantask.core.extensions.loadFromSp
import ir.aliranjbarzadeh.nikantask.core.extensions.observe
import ir.aliranjbarzadeh.nikantask.core.extensions.saveToSp
import ir.aliranjbarzadeh.nikantask.databinding.ActivityAuthBinding
import ir.aliranjbarzadeh.nikantask.databinding.LoadingBinding
import ir.aliranjbarzadeh.nikantask.domain.Loading
import ir.aliranjbarzadeh.nikantask.domain.ResponseResult
import ir.aliranjbarzadeh.nikantask.domain.StatusCode
import ir.aliranjbarzadeh.nikantask.presentation.MainActivity

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>(R.layout.activity_auth) {

	private val viewModel: AuthViewModel by viewModels()

	private lateinit var loadingBinding: LoadingBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()
		super.onCreate(savedInstanceState)

		val token = loadFromSp<String?>(AppConfig.SessionKeys.TOKEN)
		if (!token.isNullOrEmpty()) {
			goToMain()
			return
		}

		binding.viewModel = viewModel

		loadingBinding = LoadingBinding.inflate(layoutInflater, null, false)

		setupObservers()

		//Init user in database
		viewModel.saveUser()

		setTitle(R.string.login)

		binding.btnLogin.setOnClickListener {
			if (viewModel.username.value.isNullOrEmpty()) {
				Toast.makeText(this, getString(R.string.enter_username), Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			if (viewModel.password.value.isNullOrEmpty()) {
				Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			viewModel.login()
		}
	}

	private fun goToMain() {
		startActivity(Intent(this, MainActivity::class.java))
		finish()
	}


	private fun setupObservers() {
		viewModel.run {
			observe(store, ::initUser)
			observe(auth, ::initAuth)
			observe(error, ::initError)
			observe(isLoading, ::initLoading)
		}
	}

	private fun initUser(result: ResponseResult.Success<Boolean>?) {
		result?.let {
			if (!it.data) {
				MaterialAlertDialogBuilder(this)
					.setCancelable(false)
					.setTitle(R.string.error)
					.setMessage(R.string.init_user_error)
					.setPositiveButton(R.string.try_again) { dialog, _ ->
						dialog.dismiss()
						viewModel.saveUser()
					}
					.setNegativeButton(R.string.close) { dialog, _ ->
						dialog.dismiss()
						finish()
					}
			}
		}
	}

	private fun initAuth(result: ResponseResult.Success<String?>?) {
		result?.let {
			logger.debug(result, "AuthLog")
			if (result.data == null) {
				MaterialAlertDialogBuilder(this)
					.setTitle(R.string.error)
					.setMessage(R.string.sth_wrong)
					.setPositiveButton(R.string.close) { dialog, _ ->
						dialog.dismiss()
					}
					.show()
			} else {
				saveToSp(AppConfig.SessionKeys.TOKEN, result.data)
				goToMain()
			}
		}
	}

	private fun initLoading(loading: Loading?) {
		loading?.let {
			toggleLoading(loading == Loading.Normal, binding.main)
		}
	}

	private fun toggleLoading(isShow: Boolean, parentView: ViewGroup) {
		if (isShow) {
			parentView.addView(loadingBinding.root)
		} else {
			try {
				parentView.removeView(loadingBinding.root)
			} catch (_: Exception) {
			}
		}
	}

	private fun initError(result: ResponseResult.Error?) {
		result?.let {
			val error = result.data
			logger.error(error, "AuthLog")

			if (error.statusCode == StatusCode.RoomStore) {
				MaterialAlertDialogBuilder(this)
					.setCancelable(false)
					.setTitle(R.string.error)
					.setMessage(R.string.init_user_error)
					.setPositiveButton(R.string.try_again) { dialog, _ ->
						dialog.dismiss()
						viewModel.saveUser()
					}
					.setNegativeButton(R.string.close) { dialog, _ ->
						dialog.dismiss()
						finish()
					}
					.show()
			} else if (error.statusCode == StatusCode.RoomAuth) {
				MaterialAlertDialogBuilder(this)
					.setTitle(R.string.error)
					.setMessage(error.message)
					.setPositiveButton(R.string.try_again) { dialog, _ ->
						dialog.dismiss()
						binding.btnLogin.callOnClick()
					}
					.setNegativeButton(R.string.close) { dialog, _ ->
						dialog.dismiss()
					}
					.show()
			}
		}
	}
}