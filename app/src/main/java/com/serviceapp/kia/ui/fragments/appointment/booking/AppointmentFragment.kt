package com.serviceapp.kia.ui.fragments.appointment.booking

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.serviceapp.kia.data.network.responses.ServicesApi
import com.serviceapp.kia.databinding.AppointmentFragmentBinding
import com.serviceapp.kia.utils.*
import com.google.gson.Gson
import com.serviceapp.kia.R
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AppointmentFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = AppointmentFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: AppointmentViewModel
    private lateinit var navController: NavController
    private lateinit var binding: AppointmentFragmentBinding
    private val factory: AppointmentViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(AppointmentViewModel::class.java)
        binding = AppointmentFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        val safeArgs: AppointmentFragmentArgs by navArgs()
        viewModel.serviceCenterId = safeArgs.id
        viewModel.serviceCenterName = safeArgs.name
        viewModel.selectedDate = safeArgs.selectedDate
        viewModel.slotName = safeArgs.slotName
        viewModel.vehicleId = safeArgs.vehicleId
        viewModel.serviceType = safeArgs.serviceType
        viewModel.serviceName = safeArgs.serviceName

        initViews()
    }

    private fun initViews() {

        binding.appointmentServiceNameTxt.text = viewModel.serviceName

        binding.backBtn.setOnClickListener {
            navController.safePopBackStack()
        }

        binding.nextBtn.setOnClickListener {
            if (viewModel.serviceId.isNotBlank() || viewModel.serviceId.isNotEmpty()) {
                viewModel.bookAppointment()
            } else {
                view?.context?.toast("Select Service")
            }
        }

        viewModel.fetchService()

        setupServices()
//        setupServicesRecyclerView()

    }

    private fun goToNxtFragment() {
        val action = AppointmentFragmentDirections.actionAppointmentFragmentToSummaryFragment(
            viewModel.bookingId, viewModel.serviceCenterName, viewModel.selectedDate, viewModel.slotName
        )
        navController.safeNavigate(action)
    }

    private fun setupServicesRecyclerView() {
        viewModel.serviceId = ""
        val adapter = AppointmentAdapter {
            println(it.isChecked)
            if (!it.isChecked) {
                viewModel.serviceId = it.service_id.toString()
                println(viewModel.serviceId)
                viewModel.updateServiceList(it)
            }
        }
        binding.appointmentTypeRecyclerView.adapter = adapter
        viewModel.liveService.observe(viewLifecycleOwner, Observer { itemList ->
            if (itemList.size > 0) {
                val json = Gson().toJson(itemList)
                val updatedList : MutableList<ServicesApi.ServicesDataResponse> = fromJson(json)
                adapter.submitList(updatedList)
            } else {
                adapter.submitList(mutableListOf())
                viewModel.serviceId = ""
            }
        })
    }

    private fun setupServices() {
        val vehicleListMap = mutableMapOf<String, String>()
        viewModel.liveService.observe(viewLifecycleOwner, Observer {
            val vehicleList = arrayListOf<String>()
            it?.let {
                it.forEach { data ->
                    vehicleList.add(data.service_name.toString())
                    vehicleListMap[data.service_name.toString()] = data.service_id.toString()
                }
            }
            val stateListAdapter = ArrayAdapter(
                requireContext(),
                R.layout.list_item_autocomplete,
                vehicleList
            )
            binding.appointmentSpinner.setAdapter(stateListAdapter)
            if (vehicleList.size > 0) {
                binding.appointmentInputLayout.isEnabled = true
            }
            binding.appointmentInputLayout.clearFocus()
            binding.appointmentSpinner.text = null
            viewModel.serviceId = ""
        })
        binding.appointmentSpinner.setOnItemClickListener { parent, _, position, _ ->
            val vehicleName = parent.getItemAtPosition(position)
            vehicleListMap[vehicleName]?.let { stateId ->
                viewModel.serviceId = stateId
            }
        }
    }

    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
        if (viewModel.bookingSuccess) {
            goToNxtFragment()
        }
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