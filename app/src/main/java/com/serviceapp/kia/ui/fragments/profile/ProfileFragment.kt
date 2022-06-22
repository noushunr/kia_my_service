package com.serviceapp.kia.ui.fragments.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.R
import com.serviceapp.kia.databinding.ProfileFragmentBinding
import com.serviceapp.kia.ui.fragments.my.vehicle.MyVehicleFragmentDirections
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ProfileFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: ProfileViewModel
    private lateinit var navController: NavController
    private lateinit var binding: ProfileFragmentBinding
    private val factory: ProfileViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        initView()
    }

    private fun initView() {
        setUpEditable()

        binding.profileSubmitBtn.setOnClickListener {
            viewModel.isEdit = !viewModel.isEdit
            setUpEditable()
        }
        binding?.profileReset.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToUpdatePasswordFragment()
            navController.safeNavigate(action)
        }
    }

    private fun setUpEditable() {
        binding.profileEmailTxt.isEnabled = false

        val submitText = binding.profileSubmitBtn.text.toString()

        if (submitText.equals(viewModel.appContext.getLocaleStringResource(R.string.update))) {
            viewModel.updateProfile()
        } else {
            binding.profileNameTxt.isEnabled = viewModel.isEdit
            binding.profileMobileTxt.isEnabled = viewModel.isEdit
            binding.profilePasswordTxt.isEnabled = viewModel.isEdit
            binding.profileConfirmPasswordTxt.isEnabled = viewModel.isEdit
            binding.profileEmailTxt.isEnabled = viewModel.isEdit
            binding.profileSubmitBtn.text = if (viewModel.isEdit) {
                viewModel.appContext.getLocaleStringResource(R.string.update)
            } else {
                viewModel.appContext.getLocaleStringResource(R.string.edit)
            }
        }
    }

    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
        view?.context?.toast(viewModel.errorMessage)
        navController.safePopBackStack()
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

        //viewModel.fetchNotification()
    }

}