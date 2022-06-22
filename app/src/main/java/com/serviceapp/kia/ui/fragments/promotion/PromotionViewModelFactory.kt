package com.serviceapp.kia.ui.fragments.promotion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serviceapp.kia.data.repositories.HomeRepository


/*
 *Created by Adithya T Raj on 06-07-2021
*/

@Suppress("UNCHECKED_CAST")
class PromotionViewModelFactory(
    private val repository: HomeRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PromotionViewModel(repository) as T
    }

}