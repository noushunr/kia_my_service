package com.serviceapp.kia.ui.fragments.auth.login

import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.LoginApi
import com.serviceapp.kia.data.network.responses.SignUpImageApi
import com.serviceapp.kia.data.repositories.UserRepository
import com.serviceapp.kia.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import timber.log.Timber

class LoginViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val appContext by lazy {
        repository.appContext
    }

    private val gson = Gson()

    var firstInflate = true

    var vehicleImgUrl = ""

    var email: String? = repository.getCustomerLoginEmail()
    var password: String? = repository.getCustomerLoginPassword()

    var isChecked = true

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    var isLogin = true


    fun login() {

        //saveBioMetricStatus(false)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Timber.d(token)
                repository.saveFireBaseDeviceToken(token)
            }
        })

        val mEmail = email
        val mPassword = password

        if (mEmail.isNullOrEmpty() || mPassword.isNullOrEmpty()) {
            errorMessage = appContext.getLocaleStringResource(R.string.all_field_mandate)
            listener?.onFailure()
            return
        }

        if (!mEmail.isValidEmail()) {
            errorMessage = appContext.getLocaleStringResource(R.string.enter_valid_email)
            listener?.onFailure()
            return
        }

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        isLogin = true

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userLogin(
                    mEmail, mPassword
                )

                checkLoginResponse(response, mEmail, mPassword)

            } catch (error: ErrorBodyException) {
                try {
                    val response : LoginApi.LoginResponse = fromJson(error.message.toString())

                    checkLoginResponse(response, mEmail, mPassword)
                } catch (e: Exception) {
                    errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    private fun checkLoginResponse(response : LoginApi.LoginResponse, mEmail: String, mPassword: String) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            repository.saveUserDetails(res)
            repository.saveLastLoginEmail(mEmail)
            repository.saveLastLoginPassword(mPassword)
            if (isChecked) {
                repository.saveCustomerLoginEmail(mEmail)
                repository.saveCustomerLoginPassword(mPassword)
            } else {
                repository.saveCustomerLoginEmail(null)
                repository.saveCustomerLoginPassword(null)
            }
            listener?.onSuccess()
        } else {
            listener?.onFailure()
        }

        println(response.toString())
    }

    fun fetchVehicleImg() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        isLogin = false

        CoRoutines.main {
            try {
                val response = repository.userSignUpVehicleImg(KEY_LOGIN_IMG)
                checkVehicleImgResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : SignUpImageApi.SignUpImageResponse = fromJson(error.message.toString())

                    checkVehicleImgResponse(response)
                } catch (e: Exception) {
                    errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    private fun checkVehicleImgResponse(response : SignUpImageApi.SignUpImageResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            if (res.signup_pic != null) {
                vehicleImgUrl = res.signup_pic!!
            }
        }
        listener?.onSuccess()

        println(response.toString())
    }

    fun saveBioMetricStatus(status: Boolean) = repository.saveBioMetricStatus(status)

    fun getBioMetricStatus() = repository.getBioMetricStatus()

    fun bioLogin() {
        email = repository.getBioLoginEmail()
        password = repository.getBioLoginPassword()
        login()
    }

}