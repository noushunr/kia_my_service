package com.serviceapp.kia.data.repositories

import android.content.Context
import com.serviceapp.kia.data.db.AppDatabase
import com.serviceapp.kia.data.network.MyApi
import com.serviceapp.kia.data.network.SafeApiRequest
import com.serviceapp.kia.data.network.responses.*
import com.serviceapp.kia.data.preferences.PreferenceProvider
import com.serviceapp.kia.utils.KEY_KIA_BOOKING_PAST
import com.serviceapp.kia.utils.KEY_KIA_BOOKING_UP_COMING


/*
 *Created by Adithya T Raj on 30-06-2021
*/

class BookingRepository(
    val appContext: Context,
    private val api: MyApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider
) : SafeApiRequest() {

    fun userToken() = prefs.getUserToken().toString()

    fun getLoginStatus() = prefs.getLoginStatus()

    suspend fun userAvailableSlot(
        service_center: String,
        service_date: String?
    ) : AvailableSlotApi.AvailableSlotResponse {
        return apiRequest {
            api.userAvailableSlot(userToken(), service_center, service_date)
        }
    }

    suspend fun userAppointmentBooking(
        vehicle: String,
        service_center: String,
        slot: String,
        date: String,
        service_type: String,
        service: String,
        note: String
    ) : AppointmentBookingApi.AppointmentBookingResponse {
        return apiRequest {
            api.userAppointmentBooking(userToken(), vehicle, service_center,
                slot, date, service_type, service, note)
        }
    }

    suspend fun userMyBookings() : MyBookingsApi.MyBookingsResponse {
        return apiRequest {
            api.userMyBookings(userToken(), null)
        }
    }

    suspend fun userPastBookings() : MyBookingsApi.MyBookingsResponse {
        return apiRequest {
            api.userMyBookings(userToken(), KEY_KIA_BOOKING_PAST)
        }
    }

    suspend fun userUpComingBookings() : MyBookingsApi.MyBookingsResponse {
        return apiRequest {
            api.userMyBookings(userToken(), KEY_KIA_BOOKING_UP_COMING)
        }
    }

    suspend fun userServiceCenter() : ServiceCenterApi.ServiceCenterResponse {
        return apiRequest {
            api.userServiceCenter(userToken())
        }
    }

    suspend fun userMyVehicles() : MyVehiclesApi.MyVehiclesResponse {
        return apiRequest {
            api.userMyVehicles(userToken())
        }
    }

    suspend fun userServiceTypeCenterwise(serviceCenterId : String) : ServiceTypeApi.ServiceTypeResponse {
        return apiRequest {
            api.userServiceTypeCenterWise(userToken(),serviceCenterId)
        }
    }

    suspend fun userServiceType() : ServiceTypeApi.ServiceTypeResponse {
        return apiRequest {
            api.userServiceType(userToken())
        }
    }

    suspend fun userServices(
        service_type: String
    ) : ServicesApi.ServicesResponse {
        return apiRequest {
            api.userServices(userToken(), service_type)
        }
    }

    suspend fun userServicePricing(
        model_id: String,
        servicetype_id: String
    ) : ServicePricingApi.ServicePricingResponse {
        return apiRequest {
            api.userServicePricing(userToken(), model_id, servicetype_id)
        }
    }

    suspend fun userVehicleModelYear() : VehicleModelApi.VehicleModelResponse {
        return apiRequest {
            api.userVehicleModelYear()
        }
    }

    suspend fun servicePricingMail(subject:String, content : String) : AccidentApi.AccidentResponse {
        return apiRequest {
            api.servicePricingMail(userToken(),subject,content)
        }
    }

    fun getLocale() = prefs.getLocale()

}