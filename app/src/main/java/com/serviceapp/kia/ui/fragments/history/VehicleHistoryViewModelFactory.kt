package com.serviceapp.kia.ui.fragments.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serviceapp.kia.data.repositories.HomeRepository


/*
 *Created by Adithya T Raj on 01-06-2021
*/

@Suppress("UNCHECKED_CAST")
class VehicleHistoryViewModelFactory(
    private val repository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VehicleHistoryViewModel(repository) as T
    }

}