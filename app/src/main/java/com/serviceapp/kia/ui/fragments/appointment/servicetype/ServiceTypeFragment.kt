package com.serviceapp.kia.ui.fragments.appointment.servicetype

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
import com.serviceapp.kia.R
import com.serviceapp.kia.databinding.ServiceTypeFragmentBinding
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ServiceTypeFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = ServiceTypeFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: ServiceTypeViewModel
    private lateinit var navController: NavController
    private lateinit var binding: ServiceTypeFragmentBinding
    private val factory: ServiceTypeViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(ServiceTypeViewModel::class.java)
        binding = ServiceTypeFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        val safeArgs: ServiceTypeFragmentArgs by navArgs()
        viewModel.serviceCenterId = safeArgs.id
        viewModel.serviceCenterName = safeArgs.name
        viewModel.selectedDate = safeArgs.selectedDate
        viewModel.slotName = safeArgs.slotName

        initViews()
    }

    private fun initViews() {
        setupVehicle()

        //setupServiceType()

        setupServiceTypeRecyclerView()

        binding.backBtn.setOnClickListener {
            navController.safePopBackStack()
        }

        binding.nextBtn.setOnClickListener {
            if (viewModel.vehicleId.isNotBlank() || viewModel.vehicleId.isNotEmpty()) {
                if (viewModel.serviceType.isNotBlank() || viewModel.serviceType.isNotEmpty()) {
                    val action = ServiceTypeFragmentDirections.actionServiceTypeFragmentToAppointmentFragment(
                        viewModel.serviceCenterId, viewModel.serviceCenterName, viewModel.selectedDate, viewModel.slotName,
                        viewModel.vehicleId, viewModel.serviceType, viewModel.serviceName
                    )
                    navController.safeNavigate(action)
                } else {
                    view?.context?.toast("Select Service")

                }
            } else {
                view?.context?.toast("Select Vehicle")
            }
        }

        viewModel.fetchVehicle()
    }

    private fun setupVehicle() {
        val vehicleListMap = mutableMapOf<String, String>()
        viewModel.liveVehicle.observe(viewLifecycleOwner, Observer {
            val vehicleList = arrayListOf<String>()
            it?.let {
                it.forEach { data ->
                    val vehicleName = if (viewModel.getLocale()) {
                        data.vehicle_model.toString()
                    } else {
                        data.vehicle_model_arab.toString()
                    }
                    vehicleList.add(vehicleName)
                    vehicleListMap[vehicleName] = data.vehicle_id.toString()
                    //vehicleList.add(data.vehicle_model.toString())
                    //vehicleListMap[data.vehicle_model.toString()] = data.vehicle_id.toString()
                }
            }
            val stateListAdapter = ArrayAdapter(
                requireContext(),
                R.layout.list_item_autocomplete,
                vehicleList
            )
            binding.serviceTypeVehicleSpinner.setAdapter(stateListAdapter)
            if (vehicleList.size > 0) {
                binding.serviceTypeVehicleInputLayout.isEnabled = true
            }
            binding.serviceTypeVehicleInputLayout.clearFocus()
            binding.serviceTypeVehicleSpinner.text = null
            viewModel.vehicleId = ""
        })
        binding.serviceTypeVehicleSpinner.setOnItemClickListener { parent, _, position, _ ->
            val vehicleName = parent.getItemAtPosition(position)
            vehicleListMap[vehicleName]?.let { stateId ->
                viewModel.vehicleId = stateId
            }
        }
    }

    private fun setupServiceTypeRecyclerView() {
        viewModel.serviceType = ""
        viewModel.serviceName = ""
        val adapter = ServiceTypeAdapter {
            viewModel.serviceType = it.servicetype_id.toString()
            viewModel.serviceName = it.servicetype_name.toString()
        }
        binding.serviceTypeRecyclerView.adapter = adapter
        viewModel.liveServiceType.observe(viewLifecycleOwner, Observer { itemList ->
            if (itemList.size > 0) {
                adapter.submitList(itemList)
            } else {
                adapter.submitList(mutableListOf())
            }
        })
    }

    /*private fun setupServiceType() {
        val vehicleListMap = mutableMapOf<String, String>()
        viewModel.liveServiceType.observe(viewLifecycleOwner, Observer {
            val vehicleList = arrayListOf<String>()
            it?.let {
                it.forEach { data ->
                    vehicleList.add(data.servicetype_name.toString())
                    vehicleListMap[data.servicetype_name.toString()] = data.servicetype_id.toString()
                }
            }
            val stateListAdapter = ArrayAdapter(
                requireContext(),
                R.layout.list_item_autocomplete,
                vehicleList
            )
            binding.serviceTypeSpinner.setAdapter(stateListAdapter)
            if (vehicleList.size > 0) {
                binding.serviceTypeInputLayout.isEnabled = true
            }
            binding.serviceTypeInputLayout.clearFocus()
            binding.serviceTypeSpinner.text = null
            viewModel.serviceType = ""
            viewModel.serviceName = ""
        })
        binding.serviceTypeSpinner.setOnItemClickListener { parent, _, position, _ ->
            val vehicleName = parent.getItemAtPosition(position)
            vehicleListMap[vehicleName]?.let { stateId ->
                viewModel.serviceType = stateId
                viewModel.serviceName = vehicleName.toString()
            }
        }
    }*/

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