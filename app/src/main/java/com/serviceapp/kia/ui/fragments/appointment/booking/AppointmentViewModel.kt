package com.serviceapp.kia.ui.fragments.appointment.booking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.ServicesApi
import com.serviceapp.kia.data.repositories.BookingRepository
import com.serviceapp.kia.utils.*

class AppointmentViewModel(
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
    var serviceId = ""
    var note : String? = null
    var bookingId = ""
    var bookingSuccess = false

    private val mutableService = MutableLiveData<MutableList<ServicesApi.ServicesDataResponse>>()

    val liveService : LiveData<MutableList<ServicesApi.ServicesDataResponse>>
        get() = mutableService

    fun fetchService() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userServices(
                    serviceType
                )

                val data = response.data
                val message = data?.message
                val status = data?.status
                val res = data?.response

                errorMessage = message.toString()

                if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
                    if (res.size > 0) {
                        mutableService.value = res!!
                    }
                    listener?.onSuccess()
                } else {
                    mutableService.value = mutableListOf()
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

    fun bookAppointment() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userAppointmentBooking(
                    vehicleId, serviceCenterId, slotName, selectedDate, serviceType, serviceId, note.toString()
                )

                val data = response.data
                val message = data?.message
                val status = data?.status
                val res = data?.response

                errorMessage = message.toString()

                if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
                    bookingId = res.appointment.toString()
                    bookingSuccess = true
                    listener?.onSuccess()
                } else {
                    if (res != null) {
                        errorMessage = res.appointment.toString()
                    }
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

    fun updateServiceList(item : ServicesApi.ServicesDataResponse) {
        println("Selected Item - "  + item.service_name)
        val itemList = mutableService.value
        val newItemList : MutableList<ServicesApi.ServicesDataResponse> = mutableListOf()
        if (itemList != null) {
            for (serviceItem in itemList) {
                println("serviceItem - "  + serviceItem.service_name)
                println("initial - "  + serviceItem.isChecked)
                if (serviceItem.service_id.equals(item.service_id)) {
                    serviceItem.isChecked = !item.isChecked
                } else {
                    serviceItem.isChecked = false
                }
                println("final - "  + serviceItem.isChecked)
                newItemList.add(serviceItem)
            }
        }
        mutableService.value = newItemList
    }

}