package com.serviceapp.kia

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.serviceapp.kia.data.db.AppDatabase
import com.serviceapp.kia.data.network.MyApi
import com.serviceapp.kia.data.network.NetworkConnectionInterceptor
import com.serviceapp.kia.data.preferences.PreferenceProvider
import com.serviceapp.kia.data.repositories.BookingRepository
import com.serviceapp.kia.data.repositories.HomeRepository
import com.serviceapp.kia.data.repositories.UserRepository
import com.serviceapp.kia.ui.dialog.ForgotPasswordDialogViewModelFactory
import com.serviceapp.kia.ui.fragments.appointment.booking.AppointmentViewModelFactory
import com.serviceapp.kia.ui.fragments.appointment.map.ServiceAppointmentViewModelFactory
import com.serviceapp.kia.ui.fragments.appointment.servicetype.ServiceTypeViewModelFactory
import com.serviceapp.kia.ui.fragments.appointment.slot.SlotAppointmentViewModelFactory
import com.serviceapp.kia.ui.fragments.appointment.summary.SummaryViewModelFactory
import com.serviceapp.kia.ui.fragments.auth.login.LoginViewModelFactory
import com.serviceapp.kia.ui.fragments.auth.signup.SignUpViewModelFactory
import com.serviceapp.kia.ui.fragments.contact.ContactUsViewModelFactory
import com.serviceapp.kia.ui.fragments.forgot.ForgotPasswordViewModelFactory
import com.serviceapp.kia.ui.fragments.history.VehicleHistoryViewModelFactory
import com.serviceapp.kia.ui.fragments.home.HomeViewModelFactory
import com.serviceapp.kia.ui.fragments.location.LocationsViewModelFactory
import com.serviceapp.kia.ui.fragments.my.booking.MyBookingViewModelFactory
import com.serviceapp.kia.ui.fragments.my.booking.past.PastBookingViewModelFactory
import com.serviceapp.kia.ui.fragments.my.booking.upcoming.UpComingBookingViewModelFactory
import com.serviceapp.kia.ui.fragments.my.vehicle.MyVehicleViewModelFactory
import com.serviceapp.kia.ui.fragments.my.vehicle.addnew.AddNewVehicleViewModelFactory
import com.serviceapp.kia.ui.fragments.notification.NotificationViewModelFactory
import com.serviceapp.kia.ui.fragments.pricing.ServicePricingViewModelFactory
import com.serviceapp.kia.ui.fragments.profile.ProfileViewModelFactory
import com.serviceapp.kia.ui.fragments.promotion.PromotionViewModelFactory
import com.serviceapp.kia.ui.fragments.splash.SplashViewModelFactory
import com.serviceapp.kia.utils.LocaleUtils
import com.serviceapp.kia.utils.ThemeManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber


/*
 *Created by Adithya T Raj on 24-06-2021
*/

class MainApplication : MultiDexApplication(), KodeinAware {

    companion object {
        lateinit var instance: MainApplication
        lateinit var pref: PreferenceProvider
            private set
        val appContext: Context
            get() {
                return instance.applicationContext
            }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        ThemeManager.applyTheme(ThemeManager.lightMode)
        pref = PreferenceProvider(appContext)
    }

    fun initAppLanguage(context: Context) {
        LocaleUtils.initialize(context, pref.getLocale().toString())
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MainApplication))

        bind() from singleton { MyApi(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { UserRepository(instance(), instance(), instance(), instance()) }
        bind() from singleton { HomeRepository(instance(), instance(), instance(), instance()) }
        bind() from singleton { BookingRepository(instance(), instance(), instance(), instance()) }

        bind() from provider { SplashViewModelFactory(instance()) }
        bind() from provider { LoginViewModelFactory(instance()) }
        bind() from provider { SignUpViewModelFactory(instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
        bind() from provider { MyVehicleViewModelFactory(instance()) }
        bind() from provider { VehicleHistoryViewModelFactory(instance()) }
        bind() from provider { ServicePricingViewModelFactory(instance()) }
        bind() from provider { MyBookingViewModelFactory(instance()) }
        bind() from provider { PastBookingViewModelFactory(instance()) }
        bind() from provider { UpComingBookingViewModelFactory(instance()) }
        bind() from provider { ServiceAppointmentViewModelFactory(instance()) }
        bind() from provider { SlotAppointmentViewModelFactory(instance()) }
        bind() from provider { AppointmentViewModelFactory(instance()) }
        bind() from provider { ServiceTypeViewModelFactory(instance()) }
        bind() from provider { SummaryViewModelFactory(instance()) }
        bind() from provider { LocationsViewModelFactory(instance()) }
        bind() from provider { PromotionViewModelFactory(instance()) }
        bind() from provider { AddNewVehicleViewModelFactory(instance()) }
        bind() from provider { ContactUsViewModelFactory(instance()) }
        bind() from provider { ProfileViewModelFactory(instance()) }
        bind() from provider { NotificationViewModelFactory(instance()) }
        bind() from provider { ForgotPasswordDialogViewModelFactory(instance()) }
        bind() from provider { ForgotPasswordViewModelFactory(instance()) }
    }

}