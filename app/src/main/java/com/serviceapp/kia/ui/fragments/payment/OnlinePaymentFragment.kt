package com.serviceapp.kia.ui.fragments.payment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serviceapp.kia.R

class OnlinePaymentFragment : Fragment() {

    companion object {
        fun newInstance() = OnlinePaymentFragment()
    }

    private lateinit var viewModel: OnlinePaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.online_payment_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OnlinePaymentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}