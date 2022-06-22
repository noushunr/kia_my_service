package com.serviceapp.kia.ui.fragments.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.MainApplication
import com.serviceapp.kia.R
import com.serviceapp.kia.data.preferences.PreferenceProvider
import com.serviceapp.kia.databinding.HomeFragmentBinding
import com.serviceapp.kia.utils.*
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class HomeFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: HomeViewModel
    private lateinit var navController: NavController
    private lateinit var binding: HomeFragmentBinding
    private val factory: HomeViewModelFactory by instance()
    private lateinit var prefs: PreferenceProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        prefs = PreferenceProvider(requireContext())

        viewModel.listener = this
        try {
            MainApplication.instance.initAppLanguage(requireActivity())
        } catch (e: Exception) {

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideProgress()

        navController = findNavController()

        try {
            MainApplication.instance.initAppLanguage(requireActivity())
        } catch (e: Exception) {

        }

        if (viewModel.isFirstTime) {
            viewModel.fetchBanner()
            viewModel.isFirstTime = false
            viewModel.fetchAccidentNumber()
            Log.d("token",prefs.getUserToken().toString())
        }

        initView()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initView() {
        viewModel.liveBanner.observe(this, Observer { item ->
            val adapter = SliderAdapter(item, requireContext())
            binding.homeImageSlider.also {
                it.stopAutoCycle()
                it.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                it.setIndicatorAnimation(IndicatorAnimationType.WORM)
                it.indicatorSelectedColor = Color.WHITE
                it.indicatorUnselectedColor = Color.GRAY
                it.scrollTimeInSec = 5
                it.setSliderAdapter(adapter)
                it.isAutoCycle = true
                it.startAutoCycle()
                it.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
            }
        })
        binding.homeAppointment.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToServiceAppointmentFragment()
            goToNextFrag(action)
        }
        binding.homeHistory.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToVehicleHistoryFragment()
            goToNextFrag(action)
        }
        binding.homePromotion.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToPromotionFragment()
            goToNextFrag(action)
        }
        binding.homePricing.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToServicePricingFragment()
            goToNextFrag(action)
        }
        binding.homeLocation.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToLocationsFragment()
            goToNextFrag(action)
        }
        binding.homePayment.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToOnlinePaymentFragment()
            //goToNextFrag(action)
        }
        binding?.homeAccident?.setOnClickListener{
            var message = getString(R.string.accident_message)
            val url = "https://api.whatsapp.com/send?phone=${viewModel.accidentNumber.value}&text=$message"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        binding?.homeSpare?.setOnClickListener {
            showPopupWindow()
        }

    }

    private fun goToNextFrag(action: NavDirections) {
        navController.safeNavigate(action)
    }

    override fun onStarted() {
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
        if (viewModel.isSpareParts) {
            Toast.makeText(requireContext(), viewModel.errorMessage, Toast.LENGTH_LONG).show()
            viewModel.isSpareParts = false
        }
    }

    override fun onFailure() {
        hideProgress()
        if (viewModel.isSpareParts) {
            Toast.makeText(requireContext(), viewModel.errorMessage, Toast.LENGTH_LONG).show()
            viewModel.isSpareParts = false
        }
    }

    override fun onError() {
        hideProgress()
        if (viewModel.isSpareParts) {
            Toast.makeText(requireContext(), viewModel.errorMessage, Toast.LENGTH_LONG).show()
            viewModel.isSpareParts = false
        }
    }

    override fun onNoNetwork() {
        hideProgress()
        if (viewModel.isSpareParts) {
            Toast.makeText(requireContext(), viewModel.errorMessage, Toast.LENGTH_LONG).show()
            viewModel.isSpareParts = false
        }
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
        showToolBarContent()
        try {
            MainApplication.instance.initAppLanguage(requireActivity())
        } catch (e: Exception) {

        }
    }

    private fun showPopupWindow() {

        val builder = AlertDialog.Builder(context,R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.popup_spare_parts,null)
        builder.setView(view)
        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val etSubject = view.findViewById<EditText>(R.id.et_subject)
        val etContent = view.findViewById<EditText>(R.id.et_content)

        val btnCancel = view.findViewById<AppCompatButton>(R.id.cancel_btn)
        val btnSubmit = view.findViewById<AppCompatButton>(R.id.submit_btn)
        btnCancel?.setOnClickListener {
            builder.dismiss()
        }
        btnSubmit?.setOnClickListener {
            var subject = etSubject?.text.toString()
            var content = etContent?.text.toString()
            if (subject.isNullOrEmpty() || content.isNullOrEmpty()){
                view?.context?.toast(context?.getLocaleStringResource(R.string.all_field_mandate)!!)
            }else{
                builder.dismiss()
                viewModel.spairPartsMail(subject, content)
            }

        }
        etEmail.setText(prefs.getUserEmail().toString())
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
}