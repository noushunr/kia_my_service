package com.serviceapp.kia.ui.fragments.appointment.summary

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.serviceapp.kia.databinding.SummaryFragmentBinding
import com.serviceapp.kia.utils.safeNavigate
import com.serviceapp.kia.utils.setMonthDateYear
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SummaryFragment : Fragment(), KodeinAware {

    companion object {
        fun newInstance() = SummaryFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: SummaryViewModel
    private lateinit var navController: NavController
    private lateinit var binding: SummaryFragmentBinding
    private val factory: SummaryViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(SummaryViewModel::class.java)
        binding = SummaryFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        //viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        val safeArgs: SummaryFragmentArgs by navArgs()
        viewModel.serviceCenterId = safeArgs.bookingId
        viewModel.serviceCenterName = safeArgs.name
        viewModel.selectedDate = safeArgs.selectedDate
        viewModel.slotName = safeArgs.slotName

        initViews()
    }

    private fun initViews() {
        binding.summaryBookingIdTxt.text = viewModel.serviceCenterId
        binding.summaryBookingDateTxt.text = setMonthDateYear(viewModel.selectedDate)
        binding.summaryBookingTimeTxt.text = viewModel.slotName
        binding.summaryBookingCenterNameTxt.text = viewModel.serviceCenterName
        binding.summaryGoToMyBooking.setOnClickListener {
            val action = SummaryFragmentDirections.actionSummaryFragmentToMyBookingFragment()
            navController.safeNavigate(action)
        }
    }

}