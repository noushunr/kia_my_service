package com.serviceapp.kia.ui.fragments.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.ContactApi
import com.serviceapp.kia.data.network.responses.MyVehiclesApi
import com.serviceapp.kia.data.network.responses.NotificationApi
import com.serviceapp.kia.data.network.responses.ServiceTypeApi
import com.serviceapp.kia.data.repositories.HomeRepository
import com.serviceapp.kia.utils.*

class ContactUsViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    private val mutableContact = MutableLiveData<MutableList<ContactApi.ContactDataResponse>>()

    val liveContact : LiveData<MutableList<ContactApi.ContactDataResponse>>
        get() = mutableContact

    fun fetchContact() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userContactInfo()

                checkResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : ContactApi.ContactResponse = fromJson(error.message.toString())

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

    private fun checkResponse(response : ContactApi.ContactResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            var newList : MutableList<ContactApi.ContactDataResponse> = mutableListOf()
            if (res.size > 0) {
                newList = res!!
            }
            mutableContact.value = newList
            listener?.onSuccess()
        } else {
            mutableContact.value = mutableListOf()
            listener?.onFailure()
        }

        println(response.toString())

    }
}