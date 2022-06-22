package com.serviceapp.kia.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.databinding.FragmentUpdatePasswordBinding
import com.serviceapp.kia.databinding.ProfileFragmentBinding
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UpdatePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpdatePasswordFragment : Fragment(), KodeinAware, NetworkListener {
    companion object {
        fun newInstance() = UpdatePasswordFragment()
    }
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    override val kodein by kodein()

    private lateinit var viewModel: ProfileViewModel
    private lateinit var navController: NavController
    private lateinit var binding: FragmentUpdatePasswordBinding
    private val factory: ProfileViewModelFactory by instance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)
        binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        binding?.profileSubmitBtn?.setOnClickListener {
            viewModel?.updatePasswordProfile()
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
}