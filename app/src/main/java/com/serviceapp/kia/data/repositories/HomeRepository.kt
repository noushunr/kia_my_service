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

class HomeRepository(
    val appContext: Context,
    private val api: MyApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest() {

    fun userToken() = prefs.getUserToken().toString()

    fun getLoginStatus() = prefs.getLoginStatus()

    suspend fun userSliders() : SlidersApi.SlidersResponse {
        return apiRequest {
            api.userSliders(userToken(), null)
        }
    }

    suspend fun userDisplayPromotion(
        stat: String
    ) : PromotionApi.PromotionResponse {
        return apiRequest {
            api.userDisplayPromotion(userToken(), stat)
        }
    }

    suspend fun userServiceCenter() : ServiceCenterApi.ServiceCenterResponse {
        return apiRequest {
            api.userServiceCenter(userToken())
        }
    }

    suspend fun userServiceType() : ServiceTypeApi.ServiceTypeResponse {
        return apiRequest {
            api.userServiceType(userToken())
        }
    }

    suspend fun userAddNewVehicle(
        vehicle_regno: String,
        vehicle_chassis_no: String,
        vehicle_model: String,
        vehicle_reg_year: String
    ) : AddNewVehicleApi.AddNewVehicleResponse {
        return apiRequest {
            api.userAddNewVehicle(userToken(), vehicle_regno, vehicle_chassis_no, vehicle_model, vehicle_reg_year)
        }
    }

    suspend fun vehicleVerifyOTP(
        cId: String, code: String
    ) : SignUpApi.SignUpResponse {
        return apiRequest {
            api.vehicleVerifyOTP(userToken(),cId, code)
        }
    }

    suspend fun vehicleResendVerifyOTP(
        cId: String
    ) : SignUpApi.SignUpResponse {
        return apiRequest {
            api.vehicleResendOTP(userToken(),cId)
        }
    }
    suspend fun userMyVehicles() : MyVehiclesApi.MyVehiclesResponse {
        return apiRequest {
            api.userMyVehicles(userToken())
        }
    }

    suspend fun userVehicleHistory(
        vehicle: String
    ) : VehicleHistoryApi.VehicleHistoryResponse {
        return apiRequest {
            api.userVehicleHistory(userToken(), vehicle)
        }
    }

    suspend fun userNotifications() : NotificationApi.NotificationResponse {
        return apiRequest {
            api.userNotifications(userToken())
        }
    }

    suspend fun userServices(
        service_type: String
    ) : ServicesApi.ServicesResponse {
        return apiRequest {
            api.userServices(userToken(), service_type)
        }
    }

    suspend fun userVehicleModelYear() : VehicleModelApi.VehicleModelResponse {
        return apiRequest {
            api.userVehicleModelYear()
        }
    }

    fun getLocale() = prefs.getLocale()

    fun getUserName() = prefs.getUserName()
    fun getUserEmail() = prefs.getUserEmail()
    fun getUserPhone() = prefs.getUserPhone()

    fun saveFireBaseDeviceToken(token : String?) = prefs.saveFireBaseDeviceToken(token)

    suspend fun userUpdateProfile(
        name: String,
        phone: String,
        createpass: String?,
        confirmpass: String?,
        email: String
    ) : UpdateProfileApi.UpdateProfileResponse {
        return apiRequest {
            api.userUpdateProfile(userToken(), name, phone, email)
        }
    }

    suspend fun userUpdatePassword(
        currentPass: String?,
        createpass: String?,
        confirmpass: String?
    ) : UpdateProfileApi.UpdateProfileResponse {
        return apiRequest {
            api.userPassword(userToken(), currentPass!!,createpass!!,confirmpass!!)
        }
    }

    /*suspend fun userUpdateProfile(
        name: String,
        phone: String,
        createpass: String,
        confirmpass: String,
        email: String
    ) : UpdateProfileApi.UpdateProfileResponse {
        return apiRequest {
            api.userUpdateProfile(userToken(), name, phone, null, null, null, createpass, confirmpass, email)
        }
    }*/

    suspend fun userMyVehicleImg() : VehicleImageApi.VehicleImageResponse {
        return apiRequest {
            api.userMyVehicleImg(userToken())
        }
    }

    suspend fun userFcmToken() : FcmApi.FcmResponse {
        val fcmToken = prefs.getFireBaseDeviceToken()
        return apiRequest {
            api.userFcmToken(userToken(), fcmToken.toString())
        }
    }

    suspend fun userVehicleModelYearV2(
        regNo: String,
        chassisNo: String
    ) : VehicleModelYearApi.VehicleModelYearResponse {
        return apiRequest {
            api.userVehicleModelYear(regNo, chassisNo)
        }
    }

    fun saveUserDetails(userDetail: UpdateProfileApi.UpdateProfileDataResponse) {
        prefs.saveUserName(userDetail.customers_name)
        prefs.saveUserEmail(userDetail.customers_mail)
        prefs.saveUserPhone(userDetail.customers_phone)
    }

    suspend fun userReadNotifications(
        id: String
    ) : NotificationApi.NotificationReadClearResponse {
        return apiRequest {
            api.userReadNotifications(userToken(), id)
        }
    }

    suspend fun userClearNotifications(
        id: String
    ) : NotificationApi.NotificationReadClearResponse {
        return apiRequest {
            api.userClearNotifications(userToken(), id)
        }
    }

    suspend fun userContactInfo() : ContactApi.ContactResponse {
        return apiRequest {
            api.userContactInfo(userToken())
        }
    }

    suspend fun userRemoveVehicle(
        id: String
    ) : RemoveVehiclesApi.RemoveVehiclesResponse {
        return apiRequest {
            api.userRemoveVehicle(userToken(), id)
        }
    }

    suspend fun getAccidentNumber() : AccidentApi.AccidentResponse {
        return apiRequest {
            api.getAccidentNumber(userToken())
        }
    }

    suspend fun sparePartsMail(subject:String, content : String) : AccidentApi.AccidentResponse {
        return apiRequest {
            api.spairPartsMail(userToken(),subject,content)
        }
    }
}