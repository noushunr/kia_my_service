package com.serviceapp.kia.ui.fragments.pricing

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.R
import com.serviceapp.kia.databinding.ServicePricingFragmentBinding
import com.serviceapp.kia.utils.*
import com.rajat.pdfviewer.PdfViewerActivity
import com.serviceapp.kia.MainApplication
import com.serviceapp.kia.ui.fragments.home.HomeFragmentDirections
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ServicePricingFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = ServicePricingFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: ServicePricingViewModel
    private lateinit var navController: NavController
    private lateinit var binding: ServicePricingFragmentBinding
    private val factory: ServicePricingViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(ServicePricingViewModel::class.java)
        binding = ServicePricingFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        initView()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initView() {
        setupVehicle()

        setupServiceType()

        viewModel.fetchVehicle()

        binding.servicePricingSearchBtn.setOnClickListener {
            checkAndSearch()
        }

        binding.servicePricingPdfCardView.setOnClickListener {
            openPdf(viewModel.pdfUrl, viewModel.serviceName)
        }

    }

    private fun checkAndSearch() {
        if (viewModel.vehicleId.isEmpty() || viewModel.vehicleId.isBlank()) {
            view?.context?.toast("Select vehicle")
            return
        }
        if (viewModel.serviceType.isEmpty() || viewModel.serviceType.isBlank()) {
            view?.context?.toast("Select service type")
            return
        }
        viewModel.fetchPdf()
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
        })
        binding.serviceTypeVehicleSpinner.setOnItemClickListener { parent, _, position, _ ->
            val vehicleName = parent.getItemAtPosition(position)
            vehicleListMap[vehicleName]?.let { stateId ->
                viewModel.vehicleId = stateId
                binding.servicePricingPdfCardView.visibility = View.GONE
            }
        }
    }

    private fun setupServiceType() {
        val vehicleListMap = mutableMapOf<String, String>()
        viewModel.liveServiceType.observe(viewLifecycleOwner, Observer {
            val vehicleList = arrayListOf<String>()
            it?.let {
                it.forEach { data ->
                    val serviceName = if (viewModel.getLocale()) {
                        data.servicetype_name.toString()
                    } else {
                        data.servicetype_name_arab.toString()
                    }
                    vehicleList.add(serviceName)
                    vehicleListMap[serviceName] = data.servicetype_id.toString()
                    //vehicleList.add(data.servicetype_name.toString())
                    //vehicleListMap[data.servicetype_name.toString()] = data.servicetype_id.toString()
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
        })
        binding.serviceTypeSpinner.setOnItemClickListener { parent, _, position, _ ->
            val vehicleName = parent.getItemAtPosition(position)
            vehicleListMap[vehicleName]?.let { stateId ->
                viewModel.serviceType = stateId
                viewModel.serviceName = vehicleName.toString()
                binding.servicePricingPdfCardView.visibility = View.GONE
            }
        }
    }

    private fun openPdf(pdfUrl: String, name: String) {
        try {
            startActivityForResult(
                PdfViewerActivity.launchPdfFromUrl(
                    requireContext(),
                    pdfUrl,
                    name,
                    "",
                    enableDownload = false
                ),100
            )
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100){
            showBookNowDialog()
        }
    }

    private fun showBookNowDialog(){
        AlertDialog.Builder(requireContext())
            .setTitle(MainApplication.appContext.getLocaleStringResource(R.string.book_appointment))
            .setMessage(MainApplication.appContext.getLocaleStringResource(R.string.book_now_message))
            .setPositiveButton(getString(R.string.book_now)) { dialog, _ ->
                dialog.dismiss()
                val action = ServicePricingFragmentDirections.actionServicePriceFragmentToServiceAppointmentFragment()
                navController.safeNavigate(action)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
        if (viewModel.isPdf) {
            binding.servicePricingPdfCardView.visibility = View.VISIBLE
            openPdf(viewModel.pdfUrl, viewModel.serviceName)
        } else {
            binding.servicePricingPdfCardView.visibility = View.GONE
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
        showToolBarContent()
    }

}