package com.serviceapp.kia.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.model.SlideModel
import com.serviceapp.kia.data.network.responses.FcmApi
import com.serviceapp.kia.data.network.responses.SlidersApi
import com.serviceapp.kia.data.repositories.HomeRepository
import com.serviceapp.kia.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.serviceapp.kia.data.network.responses.AccidentApi
import com.serviceapp.kia.data.network.responses.ContactApi
import timber.log.Timber

class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    var isFirstTime = true
    var isSpareParts = false

    private val mutableBanner = MutableLiveData<MutableList<SlideModel>>()

    val liveBanner : LiveData<MutableList<SlideModel>>
        get() = mutableBanner
    var accidentNumber = MutableLiveData<String> ()
    var spairPartsMessage = MutableLiveData<String> ()
    fun fetchBanner() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Timber.d(token)
                repository.saveFireBaseDeviceToken(token)
            }
        })

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userSliders()

                val data = response.data
                val message = data?.message
                val status = data?.status
                val res = data?.response

                errorMessage = message.toString()

                if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
                    if (res.size > 0) {
                        setBanner(res)
                    }
                    listener?.onSuccess()
                } else {
                    listener?.onFailure()
                }

                println(response.toString())

                saveFcmToken()

            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
                isFirstTime = true
            }
        }

    }

    private fun setBanner(banner: MutableList<SlidersApi.SlidersDataResponse>) {
        if (banner.isEmpty()) {
            return
        }
        val slideList = mutableListOf<SlideModel>()
        for (list in banner) {
            slideList.add(SlideModel(list.slider_pic.toString()))
        }
        mutableBanner.value = slideList
    }

    fun saveFcmToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Timber.d(token)
                repository.saveFireBaseDeviceToken(token)
            }
        })

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        //listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userFcmToken()

                checkFcmResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : FcmApi.FcmResponse = fromJson(error.message.toString())

                    checkFcmResponse(response)
                } catch (e: Exception) {
                    errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
                isFirstTime = true
            }
        }

    }

    private fun checkFcmResponse(response : FcmApi.FcmResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            //listener?.onSuccess()
        } else {
            listener?.onFailure()
        }

        println(response.toString())
    }


    fun fetchAccidentNumber() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.getAccidentNumber()

                checkResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : AccidentApi.AccidentResponse = fromJson(error.message.toString())

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

    private fun checkResponse(response : AccidentApi.AccidentResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            accidentNumber?.postValue(res.accidentnumber_number!!)
            listener?.onSuccess()
        } else {
            accidentNumber?.postValue("")
            listener?.onSuccess()
        }

        println(response.toString())

    }


    fun spairPartsMail(subject:String,content:String) {
        isSpareParts = true
        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.sparePartsMail(subject, content)

                checkSpairResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : AccidentApi.AccidentResponse = fromJson(error.message.toString())

                    checkSpairResponse(response)
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

    private fun checkSpairResponse(response : AccidentApi.AccidentResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE)) {
            errorMessage = appContext.getLocaleStringResource(R.string.enquiry_sent)
            listener?.onSuccess()
        } else {
//            spairPartsMessage?.postValue(errorMessage)
            listener?.onFailure()
        }

        println(response.toString())

    }
}