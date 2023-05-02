package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    var darkTheme = false
    companion object {
        const val DARK_THEME = "key_for_dark_theme"
        const val APPLICATION_PREFERENCES = "application_preferences"
        lateinit var applicationPrefs : SharedPreferences
    }
    override fun onCreate() {
        super.onCreate()
        applicationPrefs = getSharedPreferences(APPLICATION_PREFERENCES, MODE_PRIVATE)
        darkTheme = applicationPrefs.getBoolean(DARK_THEME, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        applicationPrefs.edit()
            .putBoolean(DARK_THEME, darkTheme)
            .apply()
    }
}
