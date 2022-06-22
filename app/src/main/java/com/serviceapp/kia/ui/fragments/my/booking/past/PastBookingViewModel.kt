package com.serviceapp.kia.ui.fragments.my.booking.past

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.MyBookingsApi
import com.serviceapp.kia.data.repositories.BookingRepository
import com.serviceapp.kia.utils.*

class PastBookingViewModel(
    private val repository: BookingRepository
) : ViewModel() {

    private val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    private val mutableBooking = MutableLiveData<MutableList<MyBookingsApi.MyBookingsDataResponse>>()

    val liveBooking : LiveData<MutableList<MyBookingsApi.MyBookingsDataResponse>>
        get() = mutableBooking

    fun fetchPastBooking() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userPastBookings()

                val data = response.data
                val message = data?.message
                val status = data?.status
                val res = data?.response

                errorMessage = message.toString()

                if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
                    if (res.size > 0) {
                        mutableBooking.value = res!!
                    }
                    listener?.onSuccess()
                } else {
                    mutableBooking.value = mutableListOf()
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
}