package com.serviceapp.kia.ui.fragments.appointment.summary

import androidx.lifecycle.ViewModel
import com.serviceapp.kia.data.repositories.BookingRepository
import com.serviceapp.kia.utils.NetworkListener

class SummaryViewModel(
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
}