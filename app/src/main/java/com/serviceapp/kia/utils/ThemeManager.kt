package com.serviceapp.kia.utils

import androidx.appcompat.app.AppCompatDelegate
import com.serviceapp.kia.MainApplication
import com.serviceapp.kia.data.preferences.PreferenceProvider


/*
 *Created by Adithya T Raj on 24-06-2021
*/

object ThemeManager {
    const val lightMode = "light"
    const val darkMode = "dark"
    const val batterySaverMode = "battery"
    const val default = "default"
    private val pref = PreferenceProvider(MainApplication.appContext)

    fun applyTheme(theme: String?) {
        pref.saveDayNightMode(theme)
        when (theme) {
            lightMode -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            darkMode -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            batterySaverMode -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
            default -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}