package com.serviceapp.kia.ui.fragments.pricing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.*
import com.serviceapp.kia.data.repositories.BookingRepository
import com.serviceapp.kia.utils.*

class ServicePricingViewModel(
    private val repository: BookingRepository
) : ViewModel() {

    private val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    var vehicleId = ""
    var serviceType = ""
    var serviceName = ""
    var pdfUrl = ""

    var isPdf = false
    var isServicePricingEnquiry = false

    private val mutableVehicle = MutableLiveData<MutableList<MyVehiclesApi.MyVehiclesDataResponse>>()
    private val mutableServiceType = MutableLiveData<MutableList<ServiceTypeApi.ServiceTypeDataResponse>>()
    private val mutableModel = MutableLiveData<MutableList<VehicleModelApi.VehicleModelDataResponse>>()

    val liveVehicle : LiveData<MutableList<MyVehiclesApi.MyVehiclesDataResponse>>
        get() = mutableVehicle
    val liveServiceType : LiveData<MutableList<ServiceTypeApi.ServiceTypeDataResponse>>
        get() = mutableServiceType
    val liveModel : LiveData<MutableList<VehicleModelApi.VehicleModelDataResponse>>
        get() = mutableModel

    fun fetchVehicle() {

        isPdf = false

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userMyVehicles()

                val data = response.data
                val message = data?.message
                val status = data?.status
                val res = data?.response

                errorMessage = message.toString()

                if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
                    if (res.size > 0) {
                        mutableVehicle.value = res!!
                    }
                    listener?.onSuccess()
                } else {
                    mutableVehicle.value = mutableListOf()
                    listener?.onFailure()
                }

                println(response.toString())

                fetchServiceType()

            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    fun fetchServiceType() {

        isPdf = false

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userServiceType()

                val data = response.data
                val message = data?.message
                val status = data?.status
                val res = data?.response

                errorMessage = message.toString()

                if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
                    if (res.size > 0) {
                        mutableServiceType.value = res!!
                    }
                    listener?.onSuccess()
                } else {
                    mutableServiceType.value = mutableListOf()
                    listener?.onFailure()
                }

                println(response.toString())

            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    fun fetchModelYear() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userVehicleModelYear()
                checkResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : VehicleModelApi.VehicleModelResponse = fromJson(error.message.toString())

                    checkResponse(response)
                } catch (e: Exception) {
                    errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    private fun checkResponse(response : VehicleModelApi.VehicleModelResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            if (res.size > 0) {
                mutableModel.value = res!!
            } else {
                mutableModel.value = mutableListOf()
            }
            listener?.onSuccess()
        } else {
            listener?.onFailure()
        }

        println(response.toString())
    }

    fun fetchPdf() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userServicePricing(vehicleId, serviceType)

                println("$vehicleId, $serviceType")

                checkPdfResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : ServicePricingApi.ServicePricingResponse = fromJson(error.message.toString())

                    checkPdfResponse(response)
                } catch (e: Exception) {
                    errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    private fun checkPdfResponse(response: ServicePricingApi.ServicePricingResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            if (res.pdf_url != null) {
                isPdf = true
                pdfUrl = res.pdf_url!!
            }
            listener?.onSuccess()
        } else {
            listener?.onFailure()
        }

        println(response.toString())

    }

    fun getLocale() : Boolean {
        val lang = repository.getLocale()
        return when (lang) {
            APP_ENG_LOCALE -> true
            else -> false
        }
    }

    fun servicePricingMail(subject:String,content:String) {
        isServicePricingEnquiry = true
        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {

                val response = repository.servicePricingMail(subject, content)

                checkServicePricingResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : AccidentApi.AccidentResponse = fromJson(error.message.toString())

                    checkServicePricingResponse(response)
                } catch (e: Exception) {
                    errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    private fun checkServicePricingResponse(response : AccidentApi.AccidentResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE)) {
            errorMessage = appContext.getLocaleStringResource(R.string.enquiry_sent)
            listener?.onSuccess()
        } else {
//            spairPartsMessage?.postValue(errorMessage)
            listener?.onFailure()
        }

        println(response.toString())

    }
}