package com.serviceapp.kia.ui.fragments.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.ServiceCenterApi
import com.serviceapp.kia.data.repositories.BookingRepository
import com.serviceapp.kia.utils.*

class LocationsViewModel(
    private val repository: BookingRepository
) : ViewModel() {

    val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    private val mutableServiceCenter = MutableLiveData<MutableList<ServiceCenterApi.ServiceCenterDataResponse>?>()

    val liveServiceCenter : MutableLiveData<MutableList<ServiceCenterApi.ServiceCenterDataResponse>?>
        get() = mutableServiceCenter

    fun fetchServiceCenter() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userServiceCenter()

                val data = response.data
                val message = data?.message
                val status = data?.status
                val res = data?.response

                errorMessage = message.toString()

                if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
                    if (res.size > 0) {
                        mutableServiceCenter.value = res
                    }
                    listener?.onSuccess()
                } else {
                    listener?.onFailure()
                }

                println(response.toString())

            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    fun getLocale() : Boolean {
        val lang = repository.getLocale()
        return when (lang) {
            APP_ENG_LOCALE -> true
            else -> false
        }
    }
}