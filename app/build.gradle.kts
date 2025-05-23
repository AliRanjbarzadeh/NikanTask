plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.hilt)
	id("androidx.navigation.safeargs.kotlin")
	id("com.google.devtools.ksp")
	id("kotlin-parcelize")
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
		multiDexEnabled = true

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

		ksp {
			arg("room.schemaLocation", "$projectDir/schemas")
		}
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
		//noinspection DataBindingWithoutKapt
		dataBinding = true
	}
}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.androidx.activity)
	implementation(libs.androidx.constraintlayout)

	// Room
	implementation(libs.androidx.room.runtime)
	ksp(libs.androidx.room.compiler)
	implementation(libs.androidx.room.ktx)

	// Coroutines
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinx.coroutines.android)

	// Navigation
	implementation(libs.androidx.navigation.fragment.ktx)
	implementation(libs.androidx.navigation.ui.ktx)

	//Hilt
	implementation(libs.hilt.android)
	ksp(libs.hilt.android.compiler)

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
	implementation(libs.persianDate)
	implementation(libs.javafaker)

	//Tests
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
}