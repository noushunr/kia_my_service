package com.serviceapp.kia.ui.fragments.my.booking

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.databinding.MyBookingFragmentBinding
import com.serviceapp.kia.ui.fragments.my.booking.past.PastBookingFragment
import com.serviceapp.kia.ui.fragments.my.booking.upcoming.UpComingBookingFragment
import com.serviceapp.kia.utils.*
import com.google.android.material.tabs.TabLayoutMediator
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class MyBookingFragment : Fragment(), KodeinAware {

    companion object {
        fun newInstance() = MyBookingFragment()

        private val fragmentList = mutableListOf<Fragment>(
            UpComingBookingFragment.newInstance(),
            PastBookingFragment.newInstance()
        )

        private val fragmentLabel = mutableListOf(
            BOOKING_UP_COMING,
            BOOKING_PAST
        )
    }

    override val kodein by kodein()

    private lateinit var viewModel: MyBookingViewModel
    private lateinit var navController: NavController
    private lateinit var binding: MyBookingFragmentBinding
    private val factory: MyBookingViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(MyBookingViewModel::class.java)
        binding = MyBookingFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        val adapter = MyBookingFragmentAdapter(this, fragmentList)
        adapter.notifyDataSetChanged()

        binding.myBookingFragViewPager.adapter = adapter
        binding.myBookingFragViewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.myBookingFragTabLayout, binding.myBookingFragViewPager) { tab, position ->
            tab.text = fragmentLabel[position]
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
        showToolBarContent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgress()
    }

}