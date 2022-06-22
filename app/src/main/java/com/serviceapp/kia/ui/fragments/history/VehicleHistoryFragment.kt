package com.serviceapp.kia.ui.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.VehicleHistoryApi.*
import com.serviceapp.kia.databinding.VehicleHistoryFragmentBinding
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class VehicleHistoryFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = VehicleHistoryFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: VehicleHistoryViewModel
    private lateinit var navController: NavController
    private lateinit var binding: VehicleHistoryFragmentBinding
    private val factory: VehicleHistoryViewModelFactory by instance()
    private lateinit var adapter: VehicleHistoryAdapter
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(VehicleHistoryViewModel::class.java)
        binding = VehicleHistoryFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        adapter = VehicleHistoryAdapter()

        initViews()
    }

    private fun initViews() {
        setupVehicle()

        binding.vehicleHistoryRecyclerView.adapter = adapter

        /*val item1 = VehicleHistoryDataResponse(null, "1")
        val item2 = VehicleHistoryDataResponse(null, "1")
        val item3 = VehicleHistoryDataResponse(null, "1")
        val item4 = VehicleHistoryDataResponse(null, "1")

        val itemList : MutableList<VehicleHistoryDataResponse> = mutableListOf()
        itemList.add(item1)
        itemList.add(item2)
        itemList.add(item3)
        itemList.add(item4)

        adapter.submitList(itemList)*/
        binding?.btnBack.setOnClickListener {
            var position = (binding.vehicleHistoryRecyclerView.layoutManager as LinearLayoutManager)
                .findFirstVisibleItemPosition()
            if (position!=0){
                binding.vehicleHistoryRecyclerView.smoothScrollToPosition(position-1)
            }
        }
        binding?.btnNext.setOnClickListener {
            var position = (binding.vehicleHistoryRecyclerView.layoutManager as LinearLayoutManager)
                .findFirstVisibleItemPosition()
            if (position!=viewModel.liveVehicleHistory.value?.size){
                binding.vehicleHistoryRecyclerView.smoothScrollToPosition(position+1)
            }
        }

        viewModel.liveVehicleHistory.observe(viewLifecycleOwner, Observer { itemList ->
            if (itemList.size > 0) {
                binding.vehicleHistoryNoData.visibility = View.GONE
                binding?.btnBack.visibility = View.VISIBLE
                binding.btnNext.visibility = View.VISIBLE

                adapter.submitList(itemList)
            } else {
                binding?.btnBack.visibility = View.GONE
                binding.btnNext.visibility = View.GONE
                binding.vehicleHistoryNoData.visibility = View.VISIBLE
                adapter.submitList(mutableListOf())
            }
        })

        viewModel.fetchVehicle()
    }

    private fun setupVehicle() {
        val vehicleListMap = mutableMapOf<String, String>()
        viewModel.liveVehicle.observe(viewLifecycleOwner, Observer {
            val vehicleList = arrayListOf<String>()
            it?.let {
                it.forEach { data ->
                    vehicleList.add(data.vehicle_model.toString())
                    //vehicleListMap[data.vehicle_model.toString()] = data.vehicle_id.toString()
                    vehicleListMap[data.vehicle_model.toString()] = data.vehicle_regno.toString()
                }
            }
            val stateListAdapter = ArrayAdapter(
                requireContext(),
                R.layout.list_item_autocomplete,
                vehicleList
            )
            binding.vehicleHistorySelectSpinner.setAdapter(stateListAdapter)
            if (vehicleList.size > 0) {
                binding.vehicleHistorySelectInputLayout.isEnabled = true
            }
            binding.vehicleHistorySelectInputLayout.clearFocus()
            binding.vehicleHistorySelectSpinner.text = null
        })
        binding.vehicleHistorySelectSpinner.setOnItemClickListener { parent, _, position, _ ->
            val vehicleName = parent.getItemAtPosition(position)
            vehicleListMap[vehicleName]?.let { stateId ->
                viewModel.fetchVehicleHistory(stateId)
            }
        }
    }

    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
        if (viewModel.isHistory) {
//            view?.context?.toast(viewModel.errorMessage)
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