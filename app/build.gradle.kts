plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.hilt)
	id("androidx.navigation.safeargs.kotlin")
	id("org.jetbrains.kotlin.kapt")
	id("kotlin-parcelize")
	id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
	namespace = "ir.aliranjbarzadeh.nikantask"
	compileSdk = 35

	defaultConfig {
		applicationId = "ir.aliranjbarzadeh.nikantask"
		minSdk = 24
		targetSdk = 35
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}
	buildFeatures {
		dataBinding = true
	}
}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.androidx.activity)
	implementation(libs.androidx.constraintlayout)

	//Network
	implementation(libs.squareup.okhttp3)
	implementation(libs.squareup.retrofit)
	implementation(libs.squareup.retrofit.gson)

	// Coroutines
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinx.coroutines.android)
	implementation(libs.retrofit2.kotlin.coroutines.adapter)

	// Navigation
	implementation(libs.androidx.navigation.fragment.ktx)
	implementation(libs.androidx.navigation.ui.ktx)

	//Hilt
	implementation(libs.hilt.android)
	kapt(libs.hilt.android.compiler)

	// Calligraphy
	implementation(libs.calligraphy3)
	implementation(libs.viewpump)

	//Others
	implementation(libs.ssp)
	implementation(libs.sdp)
	implementation(libs.androidx.core.splashscreen)
	implementation(libs.androidx.multidex)
	implementation(libs.keyboardObserver)
	implementation(libs.hawk)
	implementation(libs.lottie)
	implementation(libs.materialDialog.core)
	implementation(libs.materialDialog.bottomsheets)
	implementation(libs.play.services.maps)
	implementation(libs.play.services.location)

	//Tests
	testImplementation(libs.mockito.core)
	testImplementation(libs.mockito.kotlin)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
}