package com.serviceapp.kia.ui.fragments.my.vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serviceapp.kia.data.repositories.HomeRepository


/*
 *Created by Adithya T Raj on 30-06-2021
*/

@Suppress("UNCHECKED_CAST")
class MyVehicleViewModelFactory(
    private val repository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyVehicleViewModel(repository) as T
    }

}