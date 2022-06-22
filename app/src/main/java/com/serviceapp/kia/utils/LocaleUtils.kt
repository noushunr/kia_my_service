package com.serviceapp.kia.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*


/*
 *Created by Adithya T Raj on 09-07-2021
*/

object LocaleUtils {

    fun initialize(context: Context, language: String) {
        val localeToSwitchTo =  Locale(language)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        Locale.setDefault(localeToSwitchTo)
        configuration.setLocale(localeToSwitchTo)
        configuration.setLayoutDirection(localeToSwitchTo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.createConfigurationContext(configuration)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}