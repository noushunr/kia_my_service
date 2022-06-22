package com.serviceapp.kia.ui.fragments.forgot

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.databinding.ForgotPasswordFragmentBinding
import com.serviceapp.kia.utils.NetworkListener
import com.serviceapp.kia.utils.progressView
import com.serviceapp.kia.utils.safePopBackStack
import com.serviceapp.kia.utils.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ForgotPasswordFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = ForgotPasswordFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var navController: NavController
    private lateinit var binding: ForgotPasswordFragmentBinding
    private val factory: ForgotPasswordViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(ForgotPasswordViewModel::class.java)
        binding = ForgotPasswordFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.forgotCancelBtn.setOnClickListener {
            navController.safePopBackStack()
        }
    }

    override fun onStarted() {
        progressView(binding.progress, View.VISIBLE)
    }

    override fun onSuccess() {
        progressView(binding.progress, View.GONE)
        view?.context?.toast(viewModel.errorMessage)
        navController.safePopBackStack()
    }

    override fun onFailure() {
        progressView(binding.progress, View.GONE)
        view?.context?.toast(viewModel.errorMessage)
    }

    override fun onError() {
        progressView(binding.progress, View.GONE)
        view?.context?.toast(viewModel.errorMessage)
    }

    override fun onNoNetwork() {
        progressView(binding.progress, View.GONE)
        view?.context?.toast(viewModel.errorMessage)
    }

}