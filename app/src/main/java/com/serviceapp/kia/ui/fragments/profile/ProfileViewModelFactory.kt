package com.serviceapp.kia.ui.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serviceapp.kia.data.repositories.HomeRepository


/*
 *Created by Adithya T Raj on 25-06-2021
*/

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(
    private val repository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(repository) as T
    }

}