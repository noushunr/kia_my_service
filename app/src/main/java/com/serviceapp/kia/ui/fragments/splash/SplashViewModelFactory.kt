package com.serviceapp.kia.ui.fragments.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serviceapp.kia.data.repositories.UserRepository


/*
 *Created by Adithya T Raj on 24-06-2021
*/

@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory(
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(repository) as T
    }

}