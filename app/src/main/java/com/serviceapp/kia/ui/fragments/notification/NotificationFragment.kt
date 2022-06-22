package com.serviceapp.kia.ui.fragments.notification

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.serviceapp.kia.data.network.responses.NotificationApi
import com.serviceapp.kia.databinding.NotificationFragmentBinding
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class NotificationFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = NotificationFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: NotificationViewModel
    private lateinit var navController: NavController
    private lateinit var binding: NotificationFragmentBinding
    private val factory: NotificationViewModelFactory by instance()
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(NotificationViewModel::class.java)
        binding = NotificationFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        adapter = NotificationAdapter {
            viewModel.clearNotification(it.id.toString())
        }

        initViews()
    }

    private fun initViews() {
        binding.notificationRecyclerView.adapter = adapter

        viewModel.liveNotification.observe(viewLifecycleOwner, Observer { itemList ->
            if (itemList.size > 0) {
                val json = Gson().toJson(itemList)
                val newList = fromJson<MutableList<NotificationApi.NotificationDataResponse>>(json)
                adapter.submitList(newList)
            } else {
                adapter.submitList(mutableListOf())
            }
        })

        /*binding.notificationClearTxt.setOnClickListener {
            viewModel.clearNotification()
        }*/
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

    override fun onResume() {
        super.onResume()
        hideKeyboard()

        viewModel.fetchNotification()
    }

}