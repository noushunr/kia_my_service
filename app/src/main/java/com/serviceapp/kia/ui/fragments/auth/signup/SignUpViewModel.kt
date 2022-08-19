package com.serviceapp.kia.ui.fragments.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.repositories.UserRepository
import com.serviceapp.kia.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.serviceapp.kia.data.network.responses.*
import timber.log.Timber

class SignUpViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var signUpListener: ModelYearListener? = null
    var errorMessage: String = ""

    var vehicleImgUrl = ""

    var email: String? = null
    var name: String? = null
    var phone: String? = null
    var phone2: String? = null
    var dob: String? = null
    var gender: String? = null
    var password: String? = null
    var confirmPass: String? = null
    var regNumber: String? = null
    var plateNumber: String? = null
    var chassisNumber: String? = null
    var modelName: String? = null
    var modelId: String? = null
    var regYear: String? = null

    var isSignUp = false
    var isModelYear = false
    var isVerify = false
    var isResendVerify = false

    var custId: String? = null
    var otp: String? = null

    private val mutableModel = MutableLiveData<MutableList<VehicleModelApi.VehicleModelDataResponse>>()
    private val mutableYear = MutableLiveData<List<String>>()
    private val mutableGender = MutableLiveData<MutableList<String>>()

    val liveModel : LiveData<MutableList<VehicleModelApi.VehicleModelDataResponse>>
        get() = mutableModel
    val liveYear : LiveData<List<String>>
        get() = mutableYear
    val liveGender : LiveData<MutableList<String>>
        get() = mutableGender

    init {
        setUpGender()
    }

    fun signUp() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Timber.d(token)
                repository.saveFireBaseDeviceToken(token)
            }
        })

        val mEmail = email
        val mName = name
        val mPhone = phone
        val mPhone2 = phone2
        val mDob = dob
        val mGender = gender
        val mPassword = password
        val mConfirmPass = confirmPass
        val mRegNumber = "$plateNumber/$regNumber"
        val mChassisNumber = chassisNumber
        val mModelName = modelName
        val mRegYear = regYear
        val mModelId = modelId

//        if (mModelName.isNullOrEmpty() || mRegYear.isNullOrEmpty() ||
//            mModelId.isNullOrEmpty()) {
//            errorMessage = appContext.getLocaleStringResource(R.string.verify_plate_chasis)
//            listener?.onFailure()
//            return
//        }

        if (mEmail.isNullOrEmpty() || mName.isNullOrEmpty() ||
            mPhone.isNullOrEmpty() || /*mPhone2.isNullOrEmpty() ||*/
//            mDob.isNullOrEmpty() || mGender.isNullOrEmpty() ||
            mPassword.isNullOrEmpty() || mConfirmPass.isNullOrEmpty()
//            mRegNumber.isNullOrEmpty() || mChassisNumber.isNullOrEmpty() ||
//            mModelName.isNullOrEmpty() || mRegYear.isNullOrEmpty() || mModelId.isNullOrEmpty()
            ) {
            errorMessage = appContext.getLocaleStringResource(R.string.enter_field_mandate)
            listener?.onFailure()
            return
        }

        if (!mEmail.isValidEmail()) {
            errorMessage = appContext.getLocaleStringResource(R.string.enter_valid_email)
            listener?.onFailure()
            return
        }

        if (mPhone.length < 8) {
            errorMessage = appContext.getLocaleStringResource(R.string.enter_valid_phone)
            listener?.onFailure()
            return
        }

        if (mPassword.length < 6) {
            errorMessage = appContext.getLocaleStringResource(R.string.strong_password)
            listener?.onFailure()
            return
        }

        if (!mPassword.equals(mConfirmPass)) {
            errorMessage = appContext.getLocaleStringResource(R.string.password_mismatch)
            listener?.onFailure()
            return
        }

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        isSignUp = true

        listener?.onStarted()

        /*FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            repository.saveFireBaseDeviceToken(it.token)
        }*/

        CoRoutines.main {
            try {

                val response = repository.userSignUp(
                    mEmail, mName, mPhone,
//                    mPhone2, mDob, mGender,
                    mPassword, mConfirmPass,
//                    mRegNumber, mChassisNumber,
//                    mModelId, mRegYear
                )

                checkSignUpResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : SignUpApi.SignUpResponse = fromJson(error.message.toString())

                    checkSignUpResponse(response)
                } catch (e: Exception) {
                    try {
                        val response : SignUpApiError.SignUpResponse = fromJson(error.message.toString())

                        checkSignUpErrorResponse(response)
                    } catch (e: Exception) {

                        errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                        println(e.message)
                        listener?.onFailure()
                    }

                }
            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    private fun checkSignUpResponse(response : SignUpApi.SignUpResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            custId = res.cid
            listener?.onSuccess()
        } else {
            if (res?.vehicle != null) {
                errorMessage = res.vehicle.toString()
            }
            listener?.onFailure()
        }

        println(response.toString())
    }

    private fun checkSignUpErrorResponse(response : SignUpApiError.SignUpResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status

        errorMessage = message.toString()
        listener?.onFailure()

        println(response.toString())
    }

    fun verifyOTP() {

        val customerId = custId
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

        isSignUp = false
        isVerify = true
        isResendVerify = false
        listener?.onStarted()

        /*FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            repository.saveFireBaseDeviceToken(it.token)
        }*/

        CoRoutines.main {
            try {

                val response = repository.userVerifyOTP(
                    customerId!!, OTP
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
            errorMessage = appContext?.getString(R.string.account_added)
            listener?.onSuccess()
        } else {
            listener?.onFailure()
        }

        println(response.toString())
    }

    fun resendOTP() {

        val customerId = custId

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        isSignUp = false
        isVerify = false
        isResendVerify = true
        listener?.onStarted()

        /*FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            repository.saveFireBaseDeviceToken(it.token)
        }*/

        CoRoutines.main {
            try {

                val response = repository.userResendVerifyOTP(
                    customerId!!
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

        listener?.onStarted()

        isSignUp = false
        isVerify = false
        isResendVerify = false
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
            //listener?.onSuccess()
            fetchVehicleImg()
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

    private fun setUpGender() {
        val itemList = mutableListOf<String>()
        val male = appContext.getLocaleStringResource(R.string.male)
        val female = appContext.getLocaleStringResource(R.string.female)
        itemList.add(male)
        itemList.add(female)
        mutableGender.value = itemList
    }

    fun getLocale() = repository.getLocale()

    fun fetchVehicleImg() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        isSignUp = false
        isVerify = false
        isResendVerify = false
        CoRoutines.main {
            try {
                val response = repository.userSignUpVehicleImg(KEY_SIGN_UP_IMG)
                checkVehicleImgResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : SignUpImageApi.SignUpImageResponse = fromJson(error.message.toString())

                    checkVehicleImgResponse(response)
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

    private fun checkVehicleImgResponse(response : SignUpImageApi.SignUpImageResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            if (res.signup_pic != null) {
                vehicleImgUrl = res.signup_pic!!
            }
        }
        listener?.onSuccess()

        println(response.toString())
    }

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

        isSignUp = false
        isVerify = false
        isResendVerify = false
        isModelYear = true

        CoRoutines.main {
            try {
                val response = repository.userVehicleModelYearV2(
                    mRegNumber, mChassisNumber
                )
                checkModelYearResponse(response)
                isModelYear = false

            } catch (error: ErrorBodyException) {
                try {
                    val response : VehicleModelYearApi.VehicleModelYearResponse = fromJson(error.message.toString())

                    checkModelYearResponse(response)
                    isModelYear = false
                } catch (e: Exception) {
                    errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                    isModelYear = false
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                isModelYear = false
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