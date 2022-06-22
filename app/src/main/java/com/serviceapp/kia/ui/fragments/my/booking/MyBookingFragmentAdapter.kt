package com.serviceapp.kia.ui.fragments.my.booking

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/*
 *Created by Adithya T Raj on 04-07-2021
*/

class MyBookingFragmentAdapter(
    fragment: Fragment,
    private val fragmentList: MutableList<Fragment>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]

}