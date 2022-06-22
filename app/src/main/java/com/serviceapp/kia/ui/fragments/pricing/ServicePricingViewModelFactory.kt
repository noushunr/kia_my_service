package com.serviceapp.kia.ui.fragments.pricing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serviceapp.kia.data.repositories.BookingRepository


/*
 *Created by Adithya T Raj on 01-06-2021
*/

@Suppress("UNCHECKED_CAST")
class ServicePricingViewModelFactory(
    private val repository: BookingRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ServicePricingViewModel(repository) as T
    }

}