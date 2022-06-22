package com.serviceapp.kia.ui.fragments.appointment.slot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.AvailableSlotApi
import com.serviceapp.kia.data.repositories.BookingRepository
import com.serviceapp.kia.utils.*

class SlotAppointmentViewModel(
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

    private val mutableSlots = MutableLiveData<List<String>>()

    val liveSlots : LiveData<List<String>>
        get() = mutableSlots

    fun fetchSlot() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                println("$serviceCenterId, $selectedDate")

                val response = repository.userAvailableSlot(
                    serviceCenterId, selectedDate
                )

                checkSlotResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : AvailableSlotApi.AvailableSlotResponse = fromJson(error.message.toString())

                    checkSlotResponse(response)
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

    private fun checkSlotResponse(response : AvailableSlotApi.AvailableSlotResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            val slots = res.split(",")
            if (slots.isNotEmpty()) {
                mutableSlots.value = slots
            } else {
                mutableSlots.value = mutableListOf()
            }
            listener?.onSuccess()
        } else {
            mutableSlots.value = mutableListOf()
            listener?.onFailure()
        }

        println(response.toString())
    }
}