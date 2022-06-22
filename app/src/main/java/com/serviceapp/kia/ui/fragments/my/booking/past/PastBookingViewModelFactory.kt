package com.serviceapp.kia.ui.fragments.my.booking.past

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serviceapp.kia.data.repositories.BookingRepository


/*
 *Created by Adithya T Raj on 04-07-2021
*/

@Suppress("UNCHECKED_CAST")
class PastBookingViewModelFactory(
    private val repository: BookingRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PastBookingViewModel(repository) as T
    }

}