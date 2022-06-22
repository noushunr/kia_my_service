package com.serviceapp.kia.ui.fragments.my.vehicle.addnew

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.R
import com.serviceapp.kia.databinding.AddNewVehicleFragmentBinding
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*

class AddNewVehicleFragment : Fragment(), KodeinAware, NetworkListener, ModelYearListener {

    companion object {
        fun newInstance() = AddNewVehicleFragment()

        private const val MAX_LENGTH = 13
        private const val MIN_LENGTH = 3
    }

    override val kodein by kodein()

    private lateinit var viewModel: AddNewVehicleViewModel
    private lateinit var navController: NavController
    private lateinit var binding: AddNewVehicleFragmentBinding
    private val factory: AddNewVehicleViewModelFactory by instance()
    private var builder : AlertDialog ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(AddNewVehicleViewModel::class.java)
        binding = AddNewVehicleFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.addNewChassisNumberTxt.setFilters(arrayOf<InputFilter>(InputFilter.AllCaps()))
        viewModel.listener = this
        viewModel.signUpListener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        initView()

        viewModel.fetchModelYear()
    }

    private fun initView() {
        val modelListMap = mutableMapOf<String, String>()
        viewModel.liveModel.observe(viewLifecycleOwner, Observer { itemList ->
            val modelList = arrayListOf<String>()
            itemList?.let {
                when (viewModel.getLocale()) {
                    APP_ARB_LOCALE -> {
                        itemList.forEach { data ->
                            modelList.add(data.vehiclemodel_name_arab.toString())
                            modelListMap[data.vehiclemodel_name_arab.toString()] = data.vehiclemodel_id.toString()
                        }
                    }
                    else -> {
                        itemList.forEach { data ->
                            modelList.add(data.vehiclemodel_name.toString())
                            modelListMap[data.vehiclemodel_name.toString()] = data.vehiclemodel_id.toString()
                        }
                    }
                }
            }
            val stateListAdapter = ArrayAdapter(
                requireContext(),
                R.layout.list_item_autocomplete,
                modelList
            )
            binding.addNewModelSpinner.setAdapter(stateListAdapter)
            if (modelList.isNotEmpty()) {
                binding.addNewModelInputLayout.isEnabled = true
            } else {
                binding.addNewModelInputLayout.isEnabled = false
                binding.addNewModelSpinner.clearFocus()
            }
            binding.addNewModelInputLayout.clearFocus()
            binding.addNewModelSpinner.text = null
            viewModel.modelName = null
        })
        binding.addNewModelSpinner.setOnItemClickListener { parent, _, position, _ ->
            viewModel.modelName = parent.getItemAtPosition(position).toString()
            modelListMap[viewModel.modelName.toString()]?.let { modelId ->
                viewModel.modelId = modelId
            }
            viewModel.setUpYearList()
            binding.addNewModelInputLayout.clearFocus()
        }
        viewModel.liveYear.observe(viewLifecycleOwner, Observer { itemList ->
            val stateListAdapter = ArrayAdapter(
                requireContext(),
                R.layout.list_item_autocomplete,
                itemList
            )
            binding.addNewYearSpinner.setAdapter(stateListAdapter)
            if (itemList.isNotEmpty()) {
                binding.addNewYearInputLayout.isEnabled = true
            } else {
                binding.addNewYearInputLayout.isEnabled = false
                binding.addNewYearInputLayout.clearFocus()
            }
            binding.addNewYearInputLayout.clearFocus()
            binding.addNewYearSpinner.text = null
            viewModel.regYear = null
        })
        binding.addNewYearSpinner.setOnItemClickListener { parent, _, position, _ ->
            viewModel.regYear = parent.getItemAtPosition(position).toString()
            binding.addNewYearInputLayout.clearFocus()
        }

        binding.addNewPlateNumber.addTextChangedListener(object : TextWatcher {

            private var updatedText: String? = null
            private var editing: Boolean = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.modelName = null
                viewModel.modelId = null
                viewModel.regYear = null
                onResetModelYear()

                var digits = text.toString()
                val length = digits.length
                if (length == 2){
                    binding.addNewPlateNumberTxt.requestFocus()
                }

//                updatedText = if (length == MIN_LENGTH && text[2].toString() != "/") {
//
//                    val startTxt = digits.substring(0, 2)
//                    val endTxt = digits.substring(2)
//
//                    String.format(Locale.getDefault(), "%s/%s", startTxt, endTxt)
//                } else {
//                    digits = digits.substring(0)
//
//                    String.format(Locale.getDefault(), "%s", digits)
//                }
            }

            override fun afterTextChanged(editable: Editable) {

//                if (editing) return
//
//                editing = true
//
//                editable.clear()
//                editable.insert(0, updatedText)
//
//                editing = false
            }

        })

        binding.addNewPlateNumberTxt.addTextChangedListener(object : TextWatcher {


            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.modelName = null
                viewModel.modelId = null
                viewModel.regYear = null
                onResetModelYear()
                var digits = text.toString()
                val length = digits.length

                if (length == 0){
                    binding.addNewPlateNumber.requestFocus()
                }

            }

            override fun afterTextChanged(editable: Editable) {

            }

        })
        binding.addNewChassisNumberTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.modelName = null
                viewModel.modelId = null
                viewModel.regYear = null
                onResetModelYear()
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }

        })
    }

    override fun onStarted() {
        hideKeyboard()
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
        if (viewModel.isNew) {
//
            showPopupWindow()
            view?.context?.toast(viewModel.errorMessage)
//            navController.safePopBackStack()
        }else if (viewModel.isVerify) {
            view?.context?.toast(viewModel.errorMessage)
            navController.safePopBackStack()
        } else if (viewModel.isResendVerify) {
            builder?.show()
            view?.context?.toast(viewModel.errorMessage)
        }
    }

    override fun onFailure() {
        hideProgress()
        if (viewModel.isVerify) {
            builder?.show()
        }
        view?.context?.toast(viewModel.errorMessage)
    }

    override fun onError() {
        hideProgress()
        if (viewModel.isVerify) {
            builder?.show()
        }
        view?.context?.toast(viewModel.errorMessage)
    }

    override fun onNoNetwork() {
        hideProgress()
        if (viewModel.isVerify) {
            builder?.show()
        }
        view?.context?.toast(viewModel.errorMessage)
    }

    override fun onResume() {
        super.onResume()
        hideToolBarContent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showToolBarContent()
    }

    override fun onSetModelYear() {
        hideProgress()
        binding.addNewModelTxt.setText(viewModel.modelName)
        binding.addNewModelYearTxt.setText(viewModel.regYear)
    }

    override fun onResetModelYear() {
        hideProgress()
        binding.addNewModelTxt.text = null
        binding.addNewModelYearTxt.text = null
    }

    private fun showPopupWindow() {

        builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.popup_otp, null)
        builder?.setView(view)
        val etOtp1 = view.findViewById<EditText>(R.id.et_otp1)
        val etOtp2 = view.findViewById<EditText>(R.id.et_otp2)
        val etOtp3 = view.findViewById<EditText>(R.id.et_otp3)
        val etOtp4 = view.findViewById<EditText>(R.id.et_otp4)
        val tvCancel = view.findViewById<TextView>(R.id.tv_cancel)
        val tvResend = view.findViewById<TextView>(R.id.tv_resend)
        etOtp1.addTextChangedListener(GenericTextWatcher(etOtp1, etOtp2))
        etOtp2.addTextChangedListener(GenericTextWatcher(etOtp2, etOtp3))
        etOtp3.addTextChangedListener(GenericTextWatcher(etOtp3, etOtp4))
        etOtp4.addTextChangedListener(GenericTextWatcher(etOtp4, null))


        etOtp1.setOnKeyListener(GenericKeyEvent(etOtp1, null))
        etOtp2.setOnKeyListener(GenericKeyEvent(etOtp2, etOtp1))
        etOtp3.setOnKeyListener(GenericKeyEvent(etOtp3, etOtp2))
        etOtp4.setOnKeyListener(GenericKeyEvent(etOtp4, etOtp3))
        val btnSubmit = view.findViewById<AppCompatButton>(R.id.verify_btn)
        tvCancel?.setOnClickListener {
            builder?.cancel()
        }
        tvResend?.setOnClickListener {
            viewModel.resendOTP()
            builder?.cancel()
        }
        btnSubmit?.setOnClickListener {
            var otp =
                etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString()
            if (otp.length == 4) {
                viewModel.otp = otp
                viewModel.verifyOTP()
                builder?.cancel()
            }
        }

        builder?.setCanceledOnTouchOutside(false)
        builder?.show()
    }

    class GenericKeyEvent internal constructor(
        private val currentView: EditText,
        private val previousView: EditText?
    ) : View.OnKeyListener {
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.et_otp1 && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }


    }

    class GenericTextWatcher internal constructor(
        private val currentView: View,
        private val nextView: View?
    ) : TextWatcher {
        override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
            val text = editable.toString()
            when (currentView.id) {
                R.id.et_otp1 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.et_otp2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.et_otp3 -> if (text.length == 1) nextView!!.requestFocus()
                //You can use EditText4 same as above to hide the keyboard
            }
        }

        override fun beforeTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { // TODO Auto-generated method stub
        }

        override fun onTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { // TODO Auto-generated method stub
        }

    }


}