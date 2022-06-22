package com.serviceapp.kia.ui.fragments.appointment.slot

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.serviceapp.kia.databinding.SlotAppointmentFragmentBinding
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*

class SlotAppointmentFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = SlotAppointmentFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: SlotAppointmentViewModel
    private lateinit var navController: NavController
    private lateinit var binding: SlotAppointmentFragmentBinding
    private val factory: SlotAppointmentViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(SlotAppointmentViewModel::class.java)
        binding = SlotAppointmentFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        val safeArgs: SlotAppointmentFragmentArgs by navArgs()
        viewModel.serviceCenterId = safeArgs.id
        viewModel.serviceCenterName = safeArgs.name

        initViews()
    }

    private fun initViews() {
        binding.slotAppointmentShowCalendarTxt.setOnClickListener {
            MaterialDialog(requireActivity()).show {
                datePicker(requireFutureDate = true) { _, date ->
                    //requireContext().toast("Selected date: ${date.formatDate()}")
                    viewModel.selectedDate = date.formatDateServer()
                    viewModel.fetchSlot()
                    binding.slotAppointmentSpinner.setAdapter(null)
                    binding.slotAppointmentSpinner.clearListSelection()
                    binding.slotAppointmentSpinner.text = null
                    binding.slotAppointmentInputLayout.clearFocus()
                    viewModel.slotName = ""
                    binding.slotAppointmentDateTxt.text = date.formatDate()
                }
            }
        }
        val calendar = Calendar.getInstance()
        viewModel.selectedDate = calendar.formatDateServer()
        viewModel.fetchSlot()
        binding.slotAppointmentCenterNameTxt.text = viewModel.serviceCenterName
        binding.slotAppointmentDateTxt.text = calendar.formatDate()

        binding.backBtn.setOnClickListener {
            navController.safePopBackStack()
        }

        binding.nextBtn.setOnClickListener {
            if (viewModel.slotName.isNotBlank() || viewModel.slotName.isNotEmpty()) {
                val action = SlotAppointmentFragmentDirections.actionSlotAppointmentFragmentToServiceTypeFragment(
                    viewModel.serviceCenterId, viewModel.serviceCenterName, viewModel.selectedDate, viewModel.slotName
                )
                navController.safeNavigate(action)
            } else {
                view?.context?.toast("Select Slot")
            }
        }

        //setUpSlots()
        setUpSlotRecyclerView()
    }

    private fun setUpSlotRecyclerView() {
        viewModel.slotName = ""
        val adapter = SlotAppointmentAdapter {
            viewModel.slotName = it
        }
        binding.slotAppointmentRecyclerView.adapter = adapter
        viewModel.liveSlots.observe(viewLifecycleOwner, Observer { slotList ->
            if (slotList.isNotEmpty()) {
                adapter.submitList(slotList)
            } else {
                adapter.submitList(mutableListOf())
                viewModel.slotName = ""
            }
        })
    }

    /*private fun setUpSlots() {
        viewModel.liveSlots.observe(viewLifecycleOwner, Observer { slotList ->
            val stateListAdapter = ArrayAdapter(
                requireContext(),
                R.layout.list_item_autocomplete,
                slotList
            )
            binding.slotAppointmentSpinner.setAdapter(stateListAdapter)
            if (slotList.isNotEmpty()) {
                binding.slotAppointmentInputLayout.isEnabled = true
            } else {
                binding.slotAppointmentInputLayout.isEnabled = false
                binding.slotAppointmentInputLayout.clearFocus()
            }
            binding.slotAppointmentInputLayout.clearFocus()
            binding.slotAppointmentSpinner.text = null
            viewModel.slotName = ""
        })
        binding.slotAppointmentSpinner.setOnItemClickListener { parent, _, position, _ ->
            viewModel.slotName = parent.getItemAtPosition(position).toString()
        }
    }*/

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
    }

}