package com.serviceapp.kia.ui.fragments.my.vehicle.addnew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serviceapp.kia.data.repositories.HomeRepository


/*
 *Created by Adithya T Raj on 30-06-2021
*/

@Suppress("UNCHECKED_CAST")
class AddNewVehicleViewModelFactory(
    private val repository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddNewVehicleViewModel(repository) as T
    }

}