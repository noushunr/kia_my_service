package com.serviceapp.kia.ui.fragments.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.serviceapp.kia.databinding.SplashFragmentBinding
import com.serviceapp.kia.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.concurrent.Executor

class SplashFragment : Fragment(), KodeinAware {

    companion object {
        fun newInstance() = SplashFragment()
    }

    override val kodein by kodein()

    private lateinit var viewModel: SplashViewModel
    private lateinit var navController: NavController
    private lateinit var binding: SplashFragmentBinding
    private val factory: SplashViewModelFactory by instance()

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, factory).get(SplashViewModel::class.java)
        binding = SplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        if (viewModel.getLocaleStatus().equals("1")) {
            lifecycleScope.launch(Dispatchers.Main) {
                delay(1000)
                goToNextFrag()
            }
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                delay(3000)
                viewModel.goNext = true
                //goToNextFrag()
                checkLogin()
            }
        }
    }

    private fun checkLogin() {
        if (!viewModel.getLoginStatus()) {
            showLangLayout()
        } else {

            /*executor = ContextCompat.getMainExecutor(requireContext())

            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        exitApp()
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        requireContext().toast(viewModel.appContext.getLocaleStringResource(R.string.authentication_succeeded))
                        goToHome()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        requireContext().toast(viewModel.appContext.getLocaleStringResource(R.string.authentication_failed))
                    }
                })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(viewModel.appContext.getLocaleStringResource(R.string.biometric_authentication))
                .setSubtitle(viewModel.appContext.getLocaleStringResource(R.string.enter_biometric_credentials_to_proceed))
                .setNegativeButtonText(viewModel.appContext.getLocaleStringResource(R.string.cancel))
                .build()

            if (viewModel.getBioMetricStatus() && viewModel.appContext.isBiometricReady()) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                goToHome()
            }*/
            goToAuth()
        }
    }

    private fun showLangLayout() {
        binding.splashLanguageMainLayout.visibility = View.VISIBLE

        binding.splashEnglishTxt.setOnClickListener {
            selectLanguage(APP_ENG_LOCALE)
        }
        binding.splashArabicTxt.setOnClickListener {
            selectLanguage(APP_ARB_LOCALE)
        }
    }

    private fun goToNextFrag() {
        if (viewModel.getLoginStatus()) {
            goToHome()
        } else {
            goToAuth()
        }
        viewModel.setLocaleStatus("0")
    }

    private fun goToHome() {
        val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        navController.safeNavigate(action)
    }

    private fun goToAuth() {
        val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        navController.safeNavigate(action)
    }

    override fun onResume() {
        super.onResume()
        //goToNextFrag()
        if (viewModel.goNext) {
            checkLogin()
        }
    }

}