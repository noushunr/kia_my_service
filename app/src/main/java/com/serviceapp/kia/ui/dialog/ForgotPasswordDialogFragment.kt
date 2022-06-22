package com.serviceapp.kia.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.databinding.ForgotPasswordDialogFragmentBinding
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ForgotPasswordDialogFragment : DialogFragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = ForgotPasswordDialogFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: ForgotPasswordDialogViewModel
    private lateinit var navController: NavController
    private lateinit var binding: ForgotPasswordDialogFragmentBinding
    private val factory: ForgotPasswordDialogViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(ForgotPasswordDialogViewModel::class.java)
        binding = ForgotPasswordDialogFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.forgotCancelBtn.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        hideKeyboard()
        hideProgress()
        navController.popBackStack()
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