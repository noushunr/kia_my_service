package com.serviceapp.kia.ui.fragments.my.vehicle

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.datatransport.runtime.backends.BackendResponse.ok
import com.serviceapp.kia.MainApplication
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.MyVehiclesApi.*
import com.serviceapp.kia.databinding.MyVehicleFragmentBinding
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.nio.file.Files.delete

class MyVehicleFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = MyVehicleFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: MyVehicleViewModel
    private lateinit var navController: NavController
    private lateinit var binding: MyVehicleFragmentBinding
    private val factory: MyVehicleViewModelFactory by instance()
    private lateinit var adapter: MyVehicleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(MyVehicleViewModel::class.java)
        binding = MyVehicleFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        adapter = MyVehicleAdapter {
            showDeleteDialog(it.vehicle_id.toString())
        }

        initViews()
    }

    private fun showDeleteDialog(vehicleId : String){
        AlertDialog.Builder(requireContext())
            .setTitle(MainApplication.appContext.getLocaleStringResource(R.string.delete))
            .setMessage(MainApplication.appContext.getLocaleStringResource(R.string.delete_confirm_dialog))
            .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                dialog.dismiss()
                viewModel.removeVehicle(vehicleId)

            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun initViews() {
        binding.myVehicleRecyclerView.adapter = adapter

        /*val item1 = MyVehiclesDataResponse( "1")
        val item2 = MyVehiclesDataResponse( "2")
        val item3 = MyVehiclesDataResponse( "3")
        val item4 = MyVehiclesDataResponse( "4")

        val itemList : MutableList<MyVehiclesDataResponse> = mutableListOf()
        itemList.add(item1)
        itemList.add(item2)
        itemList.add(item3)
        itemList.add(item4)*/

        viewModel.liveVehicle.observe(viewLifecycleOwner, Observer { itemList ->
            if (itemList.size > 0) {
                adapter.submitList(itemList)
            } else {
                adapter.submitList(mutableListOf())
            }
        })

        binding.myVehicleAddNewBtn.setOnClickListener {
            val action = MyVehicleFragmentDirections.actionMyVehicleFragmentToAddNewVehicleFragment()
            navController.safeNavigate(action)
        }

    }

    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
        if (viewModel.isImg) {
            loadCarImage(binding.myVehicleImg, viewModel.vehicleImgUrl, R.drawable.car1)
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

        if (viewModel.vehicleImgUrl.isNotBlank() || viewModel.vehicleImgUrl.isNotEmpty()) {
            loadCarImage(binding.myVehicleImg, viewModel.vehicleImgUrl, R.drawable.car1)
        }

        viewModel.fetchVehicle()
    }

}