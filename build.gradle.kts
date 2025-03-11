// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
	dependencies {
		classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.8")
		classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
	}
}

plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.kotlin.android) apply false
}