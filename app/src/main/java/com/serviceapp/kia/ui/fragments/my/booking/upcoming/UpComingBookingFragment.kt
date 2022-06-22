package com.serviceapp.kia.ui.fragments.my.booking.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.databinding.UpComingBookingFragmentBinding
import com.serviceapp.kia.ui.fragments.my.booking.MyBookingAdapter
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class UpComingBookingFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = UpComingBookingFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: UpComingBookingViewModel
    private lateinit var navController: NavController
    private lateinit var binding: UpComingBookingFragmentBinding
    private val factory: UpComingBookingViewModelFactory by instance()
    private lateinit var adapter: MyBookingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(UpComingBookingViewModel::class.java)
        binding = UpComingBookingFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        adapter = MyBookingAdapter()

        initViews()
    }

    private fun initViews() {
        viewModel.fetchUpComingBooking()

        binding.upComingBookingRecyclerView.adapter = adapter

        /*val item1 = MyBookingsApi.MyBookingsDataResponse(null, "1")
        val item2 = MyBookingsApi.MyBookingsDataResponse(null,"2")

        val itemList : MutableList<MyBookingsApi.MyBookingsDataResponse> = mutableListOf()
        itemList.add(item1)
        itemList.add(item2)

        adapter.submitList(itemList)*/

        viewModel.liveBooking.observe(viewLifecycleOwner, Observer { itemList ->
            if (itemList.size > 0) {
                adapter.submitList(itemList)
            } else {
                adapter.submitList(mutableListOf())
            }
        })

    }

    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
    }

    override fun onFailure() {
        hideProgress()
        view?.context?.toast(viewModel.errorMessage)
    }

    override fun onError() {
        hideProgress()
        view?.context?.toast(viewModel.errorMessage)
    }

    override fun onNoNetwork() {
        hideProgress()
        view?.context?.toast(viewModel.errorMessage)
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

}