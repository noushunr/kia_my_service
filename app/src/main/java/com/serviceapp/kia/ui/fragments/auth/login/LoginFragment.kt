package com.serviceapp.kia.ui.fragments.auth.login

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.R
import com.serviceapp.kia.databinding.LoginFragmentBinding
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.concurrent.Executor

class LoginFragment : Fragment(), KodeinAware, NetworkListener {

    companion object {
        fun newInstance() = LoginFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: LoginViewModel
    private lateinit var navController: NavController
    private lateinit var binding: LoginFragmentBinding
    private val factory: LoginViewModelFactory by instance()

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.listener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        //viewModel.saveBioMetricStatus(false)

        println(viewModel.getBioMetricStatus())
        println(viewModel.appContext.isBiometricReady())

        if (viewModel.getBioMetricStatus() && viewModel.appContext.isBiometricReady()) {
            binding.loginBiometricsLayout.visibility = View.VISIBLE

            executor = ContextCompat.getMainExecutor(requireContext())

            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Timber.e("onAuthenticationError")
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Timber.e("onAuthenticationSucceeded")
                        //viewModel.login()
                        //viewModel.saveBioMetricStatus(true)
                        viewModel.bioLogin()
                        requireContext().toast(viewModel.appContext.getLocaleStringResource(R.string.authentication_succeeded))
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Timber.e("onAuthenticationFailed")
                        requireContext().toast(viewModel.appContext.getLocaleStringResource(R.string.authentication_failed))
                    }
                })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(requireActivity().getLocaleStringResource(R.string.biometric_authentication))
                .setSubtitle(requireActivity().getLocaleStringResource(R.string.enter_biometric_credentials_to_proceed))
                .setNegativeButtonText(requireActivity().getLocaleStringResource(R.string.cancel))
                .build()
        } else {
            binding.loginBiometricsLayout.visibility = View.GONE
        }

        binding.loginGoToSignUp.setOnClickListener {
            goToSignUp()
        }

        binding.loginForgotTxt.setOnClickListener {
            goToForgot()
        }

        binding.loginBiometricsLayout.setOnClickListener {
            /*if (viewModel.email.isNullOrEmpty() || viewModel.password.isNullOrBlank()) {
                viewModel.login()
            } else {
                checkBiometric()
            }*/
            checkBiometric()
        }

        binding.loginRememberCheckBox.setOnCheckedChangeListener { _, bool ->
            viewModel.isChecked = bool
        }
    }

    private fun goToHome() {
        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        navController.safeNavigate(action)
    }

    private fun goToSignUp() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
        navController.safeNavigate(action)
    }

    private fun goToForgot() {
        val action = LoginFragmentDirections.actionLoginFragmentToForgotPasswordDialogFragment2()
        navController.safeNavigate(action)
    }

    private fun checkBiometric() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Timber.d("App can authenticate using biometrics.")
                biometricPrompt.authenticate(promptInfo)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                try {
                    /*val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG)
                    }
                    startActivity(enrollIntent)*/
                    val previewRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        if (it.resultCode == AppCompatActivity.RESULT_OK) {
                            val list = it.data
                            // do whatever with the data in the callback
                        }
                    }
                    val enrollIntent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                    previewRequest.launch(enrollIntent)
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Timber.d("No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Timber.d("Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                //
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                //
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                val previewRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == AppCompatActivity.RESULT_OK) {
                        val list = it.data
                        // do whatever with the data in the callback
                    }
                }
                val enrollIntent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                previewRequest.launch(enrollIntent)
            }
            else -> {
                val previewRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == AppCompatActivity.RESULT_OK) {
                        val list = it.data
                        // do whatever with the data in the callback
                    }
                }
                val enrollIntent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                previewRequest.launch(enrollIntent)
            }
        }

    }

    override fun onStarted() {
        hideKeyboard()
        showProgress()
    }

    override fun onSuccess() {
        hideProgress()
        if (viewModel.isLogin) {
            goToHome()
        } else {
            loadCarImage(binding.loginCarImg, viewModel.vehicleImgUrl)
        }
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
        if (viewModel.firstInflate) {
            viewModel.fetchVehicleImg()
        } else {
            loadCarImage(binding.loginCarImg, viewModel.vehicleImgUrl)
        }
        viewModel.firstInflate = false
    }

}