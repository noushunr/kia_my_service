package com.serviceapp.kia.ui.fragments.appointment.servicetype

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serviceapp.kia.data.repositories.BookingRepository


/*
 *Created by Adithya T Raj on 04-07-2021
*/

@Suppress("UNCHECKED_CAST")
class ServiceTypeViewModelFactory(
    private val repository: BookingRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ServiceTypeViewModel(repository) as T
    }

}