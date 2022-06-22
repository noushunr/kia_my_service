package com.serviceapp.kia.ui.fragments.my.booking

import androidx.lifecycle.ViewModel
import com.serviceapp.kia.data.repositories.BookingRepository

class MyBookingViewModel(
    private val repository: BookingRepository
) : ViewModel() {

    private val appContext by lazy {
        repository.appContext
    }
}