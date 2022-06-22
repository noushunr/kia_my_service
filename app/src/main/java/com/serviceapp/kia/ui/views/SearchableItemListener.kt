package com.serviceapp.kia.ui.views


/*
 *Created by Adithya T Raj on 04-07-2021
*/

interface SearchableItemListener {
    fun onSearchableItemClicked(item: SearchableItem, position: Int)
    fun onDismissDialog()
}