package com.serviceapp.kia.data.repositories

import android.content.Context
import com.serviceapp.kia.data.db.AppDatabase
import com.serviceapp.kia.data.network.MyApi
import com.serviceapp.kia.data.network.SafeApiRequest
import com.serviceapp.kia.data.network.responses.*
import com.serviceapp.kia.data.preferences.PreferenceProvider


/*
 *Created by Adithya T Raj on 24-06-2021
*/

class UserRepository(
    val appContext: Context,
    private val api: MyApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest() {

    fun getLoginStatus() = prefs.getLoginStatus()

    suspend fun userLogin(
        name: String,
        password: String
    ) : LoginApi.LoginResponse {
        return apiRequest {
            api.userLogin(name, password)
        }
    }

    fun saveUserDetails(userDetail: LoginApi.LoginDataResponse) {
        prefs.saveUserName(userDetail.username)
        prefs.saveUserEmail(userDetail.usermail)
        prefs.saveUserPhone(userDetail.usermobile)
        prefs.saveUserToken(userDetail.token)
        prefs.saveLoginStatus(true)
    }

    fun logoutUser() {
        prefs.saveUserName(null)
        prefs.saveUserEmail(null)
        prefs.saveUserPhone(null)
        prefs.saveUserToken(null)
        prefs.saveLoginStatus(false)
    }

    suspend fun userSignUp(
        mailId: String, name: String, phone: String,
        phone2: String?, dob: String, gender: String,
        createpass: String, confirmpass: String,
        vehicle_regno: String, vehicle_chassis_no: String,
        vehicle_model: String, vehicle_reg_year: String
    ) : SignUpApi.SignUpResponse {
        return apiRequest {
            api.userSignUp(mailId, name, phone, phone2, dob,
                gender, createpass, confirmpass, vehicle_regno,
                vehicle_chassis_no, vehicle_model, vehicle_reg_year)
        }
    }

    suspend fun userVerifyOTP(
        cId: String, code: String
    ) : SignUpApi.SignUpResponse {
        return apiRequest {
            api.verifyOTP(cId, code)
        }
    }

    suspend fun userResendVerifyOTP(
        cId: String
    ) : SignUpApi.SignUpResponse {
        return apiRequest {
            api.resendOTP(cId)
        }
    }

    suspend fun userVehicleModelYear() : VehicleModelApi.VehicleModelResponse {
        return apiRequest {
            api.userVehicleModelYear()
        }
    }

    fun saveFireBaseDeviceToken(token : String?) = prefs.saveFireBaseDeviceToken(token)

    fun getLocale() = prefs.getLocale()

    fun getLocaleStatus() = prefs.getLocaleStatus()

    fun saveLocaleStatus(status: String) = prefs.saveLocaleStatus(status)

    suspend fun userSignUpVehicleImg(
        type : String
    ) : SignUpImageApi.SignUpImageResponse {
        return apiRequest {
            api.userSignUpVehicleImg(type)
        }
    }

    suspend fun userForgotPassword(
        email: String
    ) : ForgotPasswordApi.ForgotPasswordResponse {
        return apiRequest {
            api.userForgotPassword(email)
        }
    }

    fun getBioMetricStatus() = prefs.getBioMetricStatus()

    fun saveBioMetricStatus(status: Boolean) = prefs.saveBioMetricStatus(status)

    fun getCustomerLoginEmail() = prefs.getCustomerLoginEmail()

    fun getCustomerLoginPassword() = prefs.getCustomerLoginPassword()

    fun saveCustomerLoginEmail(email: String?) = prefs.saveCustomerLoginEmail(email)

    fun saveCustomerLoginPassword(pass: String?) = prefs.saveCustomerLoginPassword(pass)

    fun saveLastLoginEmail(email: String?) = prefs.saveLastLoginEmail(email)

    fun saveLastLoginPassword(pass: String?) = prefs.saveLastLoginPassword(pass)

    fun getBioLoginEmail() = prefs.getBioEmail()

    fun getBioLoginPassword() = prefs.getBioPassword()

    suspend fun userVehicleModelYearV2(
        regNo: String,
        chassisNo: String
    ) : VehicleModelYearApi.VehicleModelYearResponse {
        return apiRequest {
            api.userVehicleModelYear(regNo, chassisNo)
        }
    }

}