package com.serviceapp.kia.ui.fragments.splash

import androidx.lifecycle.ViewModel
import com.serviceapp.kia.data.repositories.UserRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber

class SplashViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val appContext by lazy {
        repository.appContext
    }

    fun getLoginStatus() = repository.getLoginStatus()

    var goNext = false

    init {
        fcmToken()
    }

    private fun fcmToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Timber.d(token)
                repository.saveFireBaseDeviceToken(token)
            }
        })

    }

    fun getLocaleStatus() = repository.getLocaleStatus()

    fun setLocaleStatus(status: String) = repository.saveLocaleStatus(status)

    fun getBioMetricStatus() = repository.getBioMetricStatus()

}