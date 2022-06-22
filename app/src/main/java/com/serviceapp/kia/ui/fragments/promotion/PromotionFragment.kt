package com.serviceapp.kia.ui.fragments.promotion

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.MainApplication
import com.serviceapp.kia.databinding.PromotionFragmentBinding
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class PromotionFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = PromotionFragment()
        private const val url = "https://www.kia.com/kw/util/promotion.html"
        private const val MAX_PROGRESS = 100
    }

    override val kodein by kodein()

    private lateinit var viewModel: PromotionViewModel
    private lateinit var navController: NavController
    private lateinit var binding: PromotionFragmentBinding
    private val factory: PromotionViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(PromotionViewModel::class.java)
        binding = PromotionFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        val settings = binding.promotionWebView.settings
        settings.javaScriptEnabled = true

        binding.promotionWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                if (newProgress < MAX_PROGRESS) {
                    showProgress()
                }
                if (newProgress == MAX_PROGRESS) {
                    hideProgress()
                }
            }
        }
        //binding.promotionWebView.loadUrl(url)
        viewModel.fetchPromotions()
    }

    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
        binding.promotionWebView.loadUrl(viewModel.promoUrl)
    }

    override fun onFailure() {
        hideProgress()
    }

    override fun onError() {
        hideProgress()
    }

    override fun onNoNetwork() {
        hideProgress()
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
        hideToolBarContent()
    }

    override fun onPause() {
        super.onPause()
        try {
            MainApplication.instance.initAppLanguage(requireActivity())
        } catch (e: Exception) {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showToolBarContent()
        try {
            binding.promotionWebView.destroy()
            MainApplication.instance.initAppLanguage(requireActivity())
        } catch (e: Exception) {

        }
    }

}