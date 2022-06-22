package com.serviceapp.kia.ui.fragments.appointment.servicetype

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.MyVehiclesApi
import com.serviceapp.kia.data.network.responses.ServiceTypeApi
import com.serviceapp.kia.data.repositories.BookingRepository
import com.serviceapp.kia.utils.*

class ServiceTypeViewModel(
    private val repository: BookingRepository
) : ViewModel() {

    private val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    var serviceCenterId = ""
    var serviceCenterName = ""
    var selectedDate = ""
    var slotName = ""
    var vehicleId = ""
    var serviceType = ""
    var serviceName = ""

    private val mutableVehicle = MutableLiveData<MutableList<MyVehiclesApi.MyVehiclesDataResponse>>()
    private val mutableServiceType = MutableLiveData<MutableList<ServiceTypeApi.ServiceTypeDataResponse>>()

    val liveVehicle : LiveData<MutableList<MyVehiclesApi.MyVehiclesDataResponse>>
        get() = mutableVehicle
    val liveServiceType : LiveData<MutableList<ServiceTypeApi.ServiceTypeDataResponse>>
        get() = mutableServiceType

    fun fetchVehicle() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userMyVehicles()

                val data = response.data
                val message = data?.message
                val status = data?.status
                val res = data?.response

                errorMessage = message.toString()

                if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
                    if (res.size > 0) {
                        mutableVehicle.value = res!!
                    }
                    listener?.onSuccess()
                } else {
                    mutableVehicle.value = mutableListOf()
                    listener?.onFailure()
                }

                println(response.toString())

                fetchServiceType()

            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    fun fetchServiceType() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userServiceTypeCenterwise(serviceCenterId)

                val data = response.data
                val message = data?.message
                val status = data?.status
                val res = data?.response

                errorMessage = message.toString()

                if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
                    if (res.size > 0) {
                        mutableServiceType.value = res!!
                    }
                    listener?.onSuccess()
                } else {
                    mutableServiceType.value = mutableListOf()
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