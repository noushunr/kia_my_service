package com.serviceapp.kia.ui.fragments.profile

import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.UpdateProfileApi
import com.serviceapp.kia.data.repositories.HomeRepository
import com.serviceapp.kia.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber

class ProfileViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    var email: String? = null
    var name: String? = null
    var phone: String? = null
    var password: String? = null
    var newPass: String? = null
    var confirmPass: String? = null

    var isEdit = false

    init {
        setUpDefault()
    }

    private fun setUpDefault() {
        email = repository.getUserEmail()
        name = repository.getUserName()
        phone = repository.getUserPhone()
    }

    fun updateProfile() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Timber.d(token)
                repository.saveFireBaseDeviceToken(token)
            }
        })

        val mEmail = email
        val mName = name
        val mPhone = phone
        val mPassword = password
        val mConfirmPass = confirmPass

        if (mEmail.isNullOrEmpty() || mName.isNullOrEmpty() ||
            mPhone.isNullOrEmpty() /*|| mPassword.isNullOrEmpty() || mConfirmPass.isNullOrEmpty()*/) {
            errorMessage = appContext.getLocaleStringResource(R.string.enter_field_mandate)
            listener?.onFailure()
            return
        }

        if (!mEmail.isValidEmail()) {
            errorMessage = appContext.getLocaleStringResource(R.string.enter_valid_email)
            listener?.onFailure()
            return
        }

        if (mPhone.length < 8) {
            errorMessage = appContext.getLocaleStringResource(R.string.enter_valid_phone)
            listener?.onFailure()
            return
        }

//        if (!mPassword.isNullOrEmpty()) {
//
//            /*if (mPassword.length < 6) {
//                errorMessage = appContext.getLocaleStringResource(R.string.strong_password)
//                listener?.onFailure()
//                return
//            }*/
//
//            if (mConfirmPass.isNullOrEmpty()) {
//                errorMessage = appContext.getLocaleStringResource(R.string.strong_new_password)
//                listener?.onFailure()
//                return
//            }
//
//            if (mConfirmPass.length < 4) {
//                errorMessage = appContext.getLocaleStringResource(R.string.strong_new_password)
//                listener?.onFailure()
//                return
//            }
//
//        }

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.userUpdateProfile(
                    mName, mPhone, mPassword, mConfirmPass, mEmail
                )

                checkResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : UpdateProfileApi.UpdateProfileResponse = fromJson(error.message.toString())

                    checkResponse(response)
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

    private fun checkResponse(response : UpdateProfileApi.UpdateProfileResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            repository.saveUserDetails(res)
            //errorMessage = res!!
            listener?.onSuccess()
        } else {
            listener?.onFailure()
        }

        println(response.toString())
    }

    fun updatePasswordProfile() {


        val mPassword = password
        val mConfirmPass = confirmPass
        val newPassword = newPass


        if (!mPassword.isNullOrEmpty()) {

            /*if (mPassword.length < 6) {
                errorMessage = appContext.getLocaleStringResource(R.string.strong_password)
                listener?.onFailure()
                return
            }*/

            if (newPassword.isNullOrEmpty()) {
                errorMessage = appContext.getLocaleStringResource(R.string.strong_new_password)
                listener?.onFailure()
                return
            }

            if (newPassword.length < 4) {
                errorMessage = appContext.getLocaleStringResource(R.string.strong_new_password)
                listener?.onFailure()
                return
            }
            if (!mConfirmPass.equals(newPassword)) {
                errorMessage = appContext.getLocaleStringResource(R.string.password_mismatch)
                listener?.onFailure()
                return
            }

        }else{
            errorMessage = appContext.getLocaleStringResource(R.string.all_field_mandate)
            listener?.onFailure()
            return
        }

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.userUpdatePassword(
                    mPassword, newPassword,mConfirmPass
                )

                checkPasswordResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : UpdateProfileApi.UpdateProfileResponse = fromJson(error.message.toString())

                    checkResponse(response)
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

    private fun checkPasswordResponse(response : UpdateProfileApi.UpdateProfileResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            repository.saveUserDetails(res)
            //errorMessage = res!!
            listener?.onSuccess()
        } else {
            listener?.onFailure()
        }

        println(response.toString())
    }
}