package com.serviceapp.kia.ui.fragments.contact

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.databinding.ContactUsFragmentBinding
import com.serviceapp.kia.utils.toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.serviceapp.kia.ui.fragments.appointment.servicetype.ServiceTypeAdapter
import com.serviceapp.kia.utils.NetworkListener
import com.serviceapp.kia.utils.hideProgress
import com.serviceapp.kia.utils.showProgress
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ContactUsFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = ContactUsFragment()
        const val CALL_NO_1 = "+96594115237"
        const val CALL_NO_2 = "+96597906850"
    }

    override val kodein by kodein()

    private lateinit var viewModel: ContactUsViewModel
    private lateinit var navController: NavController
    private lateinit var binding: ContactUsFragmentBinding
    private val factory: ContactUsViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(ContactUsViewModel::class.java)
        binding = ContactUsFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        hideProgress()

        viewModel.fetchContact()

        initView()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initView() {
        binding.contactCallBtn1.setOnClickListener {
            runtimePermission(CALL_NO_1)
        }
        binding.contactCallBtn2.setOnClickListener {
            runtimePermission(CALL_NO_2)
        }
        val adapter = ContactUsAdapter {
            if (it.contact_phon != null) {
                runtimePermission(it.contact_phon.toString())
            }
        }
        binding.contactRecyclerView.adapter = adapter

        viewModel.liveContact.observe(viewLifecycleOwner, Observer { itemList ->
            if (itemList.size > 0) {
                adapter.submitList(itemList)
            } else {
                adapter.submitList(mutableListOf())
            }
        })
    }

    private fun runtimePermission(phoneNumber: String) {
        Dexter.withContext(requireContext())
            .withPermissions(Manifest.permission.CALL_PHONE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        startCall(phoneNumber)
                    } else if (report.isAnyPermissionPermanentlyDenied) {
                        view?.context?.toast("Call permission denied")
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            })
            .onSameThread()
            .check()
    }

    private fun startCall(phoneNumber: String) {
        val phone = "tel:$phoneNumber"
        val uri = Uri.parse(phone)
        val intent = Intent(Intent.ACTION_CALL, uri)
        startActivity(intent)
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

}