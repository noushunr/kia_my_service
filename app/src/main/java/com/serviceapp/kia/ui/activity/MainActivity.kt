package com.serviceapp.kia.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.serviceapp.kia.MainApplication
import com.serviceapp.kia.R
import com.serviceapp.kia.data.preferences.PreferenceProvider
import com.serviceapp.kia.data.repositories.UserRepository
import com.serviceapp.kia.databinding.ActivityMainBinding
import com.serviceapp.kia.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity(), KodeinAware  {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding

    private val prefs = PreferenceProvider(MainApplication.appContext)

    override val kodein by kodein()
    private val repository by instance<UserRepository>()

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private lateinit var menuNav: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApplication.instance.initAppLanguage(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.main_nav_host)

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment, R.id.loginFragment,
            R.id.forgotPasswordDialogFragment, //R.id.profileFragment,
            R.id.signUpFragment, R.id.splashFragment)
            .setOpenableLayout(binding.mainDrawerLayout)
            .build()

        menuNav = binding.mainNavigationView.menu.findItem(R.id.nav_verification).actionView.findViewById<SwitchCompat>(R.id.menu_nav_verification_switch)

        if (prefs.getBioMetricStatus() ) {
            menuNav.isChecked = true
        }
        menuNav.setOnCheckedChangeListener { _, b ->
            if (b) {
                Timber.e("Biometric")
                showBiometric()
            } /*else {
                Timber.e("setOnCheckedChangeListener false")
                prefs.saveBioMetricStatus(false)
            }*/
        }

        visibilityNavElements(navController)

        binding.mainToolbarLanguageBtn.setOnClickListener {
            switchLanguage()
        }

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    menuNav.isChecked = false
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    enableBioLogin()
                    this@MainActivity.toast(this@MainActivity.getLocaleStringResource(R.string.authentication_succeeded))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    menuNav.isChecked = false
                    this@MainActivity.toast(this@MainActivity.getLocaleStringResource(R.string.authentication_failed))
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(this@MainActivity.getLocaleStringResource(R.string.biometric_authentication))
            .setSubtitle(this@MainActivity.getLocaleStringResource(R.string.enter_biometric_credentials_to_proceed))
            .setNegativeButtonText(this@MainActivity.getLocaleStringResource(R.string.cancel))
            .build()
    }

    private fun setupNavControl() {

        setSupportActionBar(binding.mainToolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.mainNavigationView.setupWithNavController(navController)

        val userName = PreferenceProvider(this).getUserName()
        val mainNavigationViewHeader = binding.mainNavigationView.getHeaderView(0)
        mainNavigationViewHeader.findViewById<TextView>(R.id.nav_customer_name_txt).text = userName

        binding.mainNavigationView.menu.findItem(R.id.nav_signout).setOnMenuItemClickListener {
            closeDrawer()
            showSignOutDialog()
            true
        }

        menuNav = binding.mainNavigationView.menu.findItem(R.id.nav_verification).actionView.findViewById<SwitchCompat>(R.id.menu_nav_verification_switch)

        if (!prefs.getBioMetricStatus()) {
            menuNav.isChecked = false
            prefs.saveBioMetricStatus(false)
        }

    }

    private fun showBiometric() {
        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Timber.d("App can authenticate using biometrics.")
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Timber.e("No biometric features available on this device.")
                menuNav.isChecked = false
                prefs.saveBioMetricStatus(false)
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Timber.e("Biometric features are currently unavailable.")
                menuNav.isChecked = false
                prefs.saveBioMetricStatus(false)
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                this.toast("Enable Finger Print or Face Unlock in your settings")
                val enrollIntent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                //this.startActivityForResult(enrollIntent, 201)
                val previewRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    //
                }
                previewRequest.launch(enrollIntent)
                menuNav.isChecked = false
                prefs.saveBioMetricStatus(false)
            }
            else -> {
                this.toast("Enable Finger Print or Face Unlock in your settings")
                Timber.e("Biometric unknown")
                menuNav.isChecked = false
                prefs.saveBioMetricStatus(false)
                val previewRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    //
                }
                val enrollIntent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                previewRequest.launch(enrollIntent)
            }
        }

    }

    private fun enableBioLogin() {
        prefs.saveBioEmail(prefs.getLastLoginEmail())
        prefs.saveBioPassword(prefs.getLastLoginPassword())
        prefs.saveBioMetricStatus(true)
        println("hiii")
    }

    private fun visibilityNavElements(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> {
                    setViewsGone()
                    binding.mainFooter.visibility = View.GONE
                }
                R.id.loginFragment -> {
                    setViewsGone()
                    binding.mainFooter.visibility = View.GONE
                }
                R.id.signUpFragment -> {
                    setViewsGone()
                    binding.mainFooter.visibility = View.GONE
                }
                R.id.homeFragment -> {
                    setupNavControl()
                    setViewsVisible()
                    navController.graph.startDestination = R.id.homeFragment
                    binding.mainFooter.visibility = View.VISIBLE
                }
                R.id.forgotPasswordDialogFragment -> {
                    setViewsGone()
                    binding.mainFooter.visibility = View.GONE
                }
                else -> {
                    setupNavControl()
                    setViewsVisible()
                    binding.mainFooter.visibility = View.GONE
                }
            }
        }
    }

    private fun setViewsVisible() {
        binding.mainAppbar.visibility = View.VISIBLE
        binding.mainToolbar.visibility = View.VISIBLE
        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setupNavControl()
    }

    private fun setViewsGone() {
        binding.mainAppbar.visibility = View.GONE
        binding.mainToolbar.visibility = View.GONE
        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        setupNavControl()
    }

    private fun switchLanguage() {
        val switchLang = when (prefs.getLocale().toString()) {
            APP_ENG_LOCALE -> APP_ARB_LOCALE
            else -> APP_ENG_LOCALE
        }
        prefs.saveLocale(switchLang)
        prefs.saveLocaleStatus("1")
        val intent = Intent(
            this,
            MainActivity::class.java
        )
        startActivity(intent)
        finish()
    }

    private fun showSignOutDialog() {

        menuNav = binding.mainNavigationView.menu.findItem(R.id.nav_verification).actionView.findViewById<SwitchCompat>(R.id.menu_nav_verification_switch)

        if (!menuNav.isChecked) {
            prefs.saveBioMetricStatus(false)
        } else {
            enableBioLogin()
        }

        AlertDialog.Builder(this)
            .setTitle(MainApplication.appContext.getLocaleStringResource(R.string.menu_signout))
            .setMessage(MainApplication.appContext.getLocaleStringResource(R.string.sign_out_confirm_dialog))
            .setPositiveButton(this.resources.getText(android.R.string.ok)) { dialog, _ ->
                dialog.dismiss()
                repository.logoutUser()
                //logoutUser()
                navController.navigate(R.id.action_global_loginFragment)
                hideKeyboard()
            }
            .setNegativeButton(this.resources.getText(android.R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun logoutUser() {
        showSignOutDialog()
        //prefs.saveUserName(null)
        //prefs.saveUserEmail(null)
        //prefs.saveUserPhone(null)
        //prefs.saveUserToken(null)
        //prefs.saveLoginStatus(false)
    }

    fun toolBarContentVisibility(visibility: Int) {
        progressView(binding.mainToolbarContent, visibility)
    }

    fun progress(visibility: Int) {
        progressView(binding.progress.loadingOverlay, visibility)
    }

    fun getProgressVisibility() = binding.progress.loadingOverlay.visibility

    fun selectLanguage(switchLang : String) {
        prefs.saveLocale(switchLang)
        prefs.saveLocaleStatus("1")
        val intent = Intent(
            this,
            MainActivity::class.java
        )
        startActivity(intent)
        finish()
    }

    fun exitApp() {
        this.finishAffinity()
    }

    fun closeDrawer() {
        when {
            binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START) -> {
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onPause() {
        super.onPause()

        menuNav = binding.mainNavigationView.menu.findItem(R.id.nav_verification).actionView.findViewById<SwitchCompat>(R.id.menu_nav_verification_switch)

        if (!menuNav.isChecked) {
            prefs.saveBioMetricStatus(false)
        } else {
            enableBioLogin()
        }
    }

    override fun onBackPressed() {
        when {
            binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START) -> {
                closeDrawer()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}