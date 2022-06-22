package com.serviceapp.kia.ui.fragments.my.vehicle.addnew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.AddNewVehicleApi
import com.serviceapp.kia.data.network.responses.SignUpApi
import com.serviceapp.kia.data.network.responses.VehicleModelApi
import com.serviceapp.kia.data.network.responses.VehicleModelYearApi
import com.serviceapp.kia.data.repositories.HomeRepository
import com.serviceapp.kia.utils.*

class AddNewVehicleViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var signUpListener: ModelYearListener? = null
    var errorMessage: String = ""

    var regNumber: String? = null
    var plateNumber: String? = null
    var chassisNumber: String? = null
    var modelName: String? = null
    var regYear: String? = null
    var modelId: String? = null

    var isNew = false
    var isVerify = false
    var isResendVerify = false

    var vehicle_id: String? = null
    var otp: String? = null

    private val mutableModel = MutableLiveData<MutableList<VehicleModelApi.VehicleModelDataResponse>>()
    private val mutableYear = MutableLiveData<List<String>>()

    val liveModel : LiveData<MutableList<VehicleModelApi.VehicleModelDataResponse>>
        get() = mutableModel
    val liveYear : LiveData<List<String>>
        get() = mutableYear

    fun addNew() {

        val mRegNumber = "$plateNumber/$regNumber"
        val mChassisNumber = chassisNumber
        val mModelName = modelName
        val mRegYear = regYear
        val mModelId = modelId

        if (mModelName.isNullOrEmpty() || mRegYear.isNullOrEmpty() ||
            mModelId.isNullOrEmpty()) {
            errorMessage = appContext.getLocaleStringResource(R.string.verify_plate_chasis)
            listener?.onFailure()
            return
        }

        if (mRegNumber.isNullOrEmpty() || mChassisNumber.isNullOrEmpty() ||
            mModelName.isNullOrEmpty() || mRegYear.isNullOrEmpty() ||
            mModelId.isNullOrEmpty()) {
            errorMessage = appContext.getLocaleStringResource(R.string.all_field_mandate)
            listener?.onFailure()
            return
        }

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        isNew= true

        CoRoutines.main {
            try {

                val response = repository.userAddNewVehicle(
                    mRegNumber, mChassisNumber,
                    mModelId, mRegYear
                )

                checkAddNewResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : AddNewVehicleApi.AddNewVehicleResponse = fromJson(error.message.toString())

                    checkAddNewResponse(response)
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

    private fun checkAddNewResponse(response : AddNewVehicleApi.AddNewVehicleResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            if (res.vehicle_id != null) {
//                errorMessage = res.Vehicle.toString()
                vehicle_id = res.vehicle_id
            }
            listener?.onSuccess()
        } else {
            if (res?.Vehicle != null) {
                errorMessage = res.Vehicle.toString()
            }
            listener?.onFailure()
        }

        println(response.toString())
    }

    fun verifyOTP() {

        val vehicleId = vehicle_id
        val OTP = otp


        if (OTP.isNullOrEmpty()) {
            errorMessage = appContext.getLocaleStringResource(R.string.enter_field_mandate)
            listener?.onFailure()
            return
        }


        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        isNew = false
        isVerify = true
        isResendVerify = false
        listener?.onStarted()

        /*FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            repository.saveFireBaseDeviceToken(it.token)
        }*/

        CoRoutines.main {
            try {

                val response = repository.vehicleVerifyOTP(
                    vehicleId!!, OTP
                )

                checkVerifyOtpResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : SignUpApi.SignUpResponse = fromJson(error.message.toString())

                    checkVerifyOtpResponse(response)
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

    private fun checkVerifyOtpResponse(response : SignUpApi.SignUpResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status


        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE)) {

            errorMessage = appContext?.getString(R.string.vehicle_add_success)
            listener?.onSuccess()
        } else {
            listener?.onFailure()
        }

        println(response.toString())
    }

    fun resendOTP() {

        val vehicleId = vehicle_id

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        isNew = false
        isVerify = false
        isResendVerify = true
        listener?.onStarted()

        /*FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            repository.saveFireBaseDeviceToken(it.token)
        }*/

        CoRoutines.main {
            try {

                val response = repository.vehicleResendVerifyOTP(
                    vehicleId!!
                )

                checkResendVerifyOtpResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : SignUpApi.SignUpResponse = fromJson(error.message.toString())

                    checkResendVerifyOtpResponse(response)
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

    private fun checkResendVerifyOtpResponse(response : SignUpApi.SignUpResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) ) {
            listener?.onSuccess()
        } else {
            listener?.onFailure()
        }

        println(response.toString())
    }
    fun fetchModelYear() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        isNew = false

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

    fun setUpYearList() {
        val itemList = mutableModel.value
        if (itemList != null) {
            var modelYear = ""
            for (item in itemList) {
                when (getLocale()) {
                    APP_ARB_LOCALE -> {
                        if (item.vehiclemodel_name_arab.equals(modelName)) {
                            modelYear = item.vehiclemodel_year.toString()
                        }
                    } else -> {
                    if (item.vehiclemodel_name.equals(modelName)) {
                        modelYear = item.vehiclemodel_year.toString()
                    }
                }
                }
            }
            val year = modelYear.split(",")
            if (year.isNotEmpty()) {
                mutableYear.value = year
            }
        }
    }

    fun getLocale() = repository.getLocale()

    fun fetchVehicleModelYear() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        val mRegNumber = "$plateNumber/$regNumber"
        val mChassisNumber = chassisNumber

        if (mRegNumber.isNullOrEmpty() || mChassisNumber.isNullOrEmpty()) {
            errorMessage = appContext.getLocaleStringResource(R.string.verify_plate_chasis_required)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userVehicleModelYearV2(
                    mRegNumber, mChassisNumber
                )
                checkModelYearResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : VehicleModelYearApi.VehicleModelYearResponse = fromJson(error.message.toString())

                    checkModelYearResponse(response)
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

    private fun checkModelYearResponse(response : VehicleModelYearApi.VehicleModelYearResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            modelName = res.vehicle_model
            modelId = res.vehicle_model_id
            regYear = res.vehicle_reg_year
            //isModelYear = true
            signUpListener?.onSetModelYear()
        } else {
            modelName = null
            modelId = null
            regYear = null
            if (res?.Vehicle != null) {
                errorMessage = res.Vehicle.toString()
            }
            signUpListener?.onResetModelYear()
            listener?.onFailure()
        }

        println(response.toString())
    }
}