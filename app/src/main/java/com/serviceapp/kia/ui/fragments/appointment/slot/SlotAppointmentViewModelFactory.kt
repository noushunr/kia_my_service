package com.serviceapp.kia.ui.fragments.appointment.slot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serviceapp.kia.data.repositories.BookingRepository


/*
 *Created by Adithya T Raj on 04-07-2021
*/

@Suppress("UNCHECKED_CAST")
class SlotAppointmentViewModelFactory(
    private val repository: BookingRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SlotAppointmentViewModel(repository) as T
    }

}