package com.serviceapp.kia.data.network

import com.serviceapp.kia.data.network.responses.*
import com.serviceapp.kia.utils.PRO_BASE_API_URL
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


/*
 *Created by Adithya T Raj on 24-06-2021
*/

interface MyApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun userLogin(
        @Field("username") name: String,
        @Field("password") password: String
    ) : Response<LoginApi.LoginResponse>

    @FormUrlEncoded
    @POST("registercustomer1")
    suspend fun userSignUp(
        @Field("mailid") mailId: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("phone2") phone2: String?,
        @Field("dob") dob: String,
        @Field("gender") gender: String,
        @Field("createpass") createpass: String,
        @Field("confirmpass") confirmpass: String,
        @Field("vehicle_regno") vehicle_regno: String,
        @Field("vehicle_chassis_no") vehicle_chassis_no: String,
        @Field("vehicle_model") vehicle_model: String,
        @Field("vehicle_reg_year") vehicle_reg_year: String
    ) : Response<SignUpApi.SignUpResponse>

    @FormUrlEncoded
    @POST("customer_sms_verify")
    suspend fun verifyOTP(
        @Field("cid") cid: String,
        @Field("code") name: String
    ) : Response<SignUpApi.SignUpResponse>

    @FormUrlEncoded
    @POST("customer_resend_sms_verify")
    suspend fun resendOTP(
        @Field("cid") cid: String
    ) : Response<SignUpApi.SignUpResponse>

    @FormUrlEncoded
    @POST("vehicle_sms_verify")
    suspend fun vehicleVerifyOTP(
        @Header("Authorization") token: String,
        @Field("vehicle_id") cid: String,
        @Field("code") name: String
    ) : Response<SignUpApi.SignUpResponse>

    @FormUrlEncoded
    @POST("vehicle_resend_sms_verify")
    suspend fun vehicleResendOTP(
        @Header("Authorization") token: String,
        @Field("vehicle_id") cid: String
    ) : Response<SignUpApi.SignUpResponse>

    @FormUrlEncoded
    @POST("display_promotion")
    suspend fun userDisplayPromotion(
        @Header("Authorization") token: String,
        @Field("stat") stat: String
    ) : Response<PromotionApi.PromotionResponse>

    @POST("servicecenters")
    suspend fun userServiceCenter(
        @Header("Authorization") token: String
    ) : Response<ServiceCenterApi.ServiceCenterResponse>

    @POST("display_servicetype")
    suspend fun userServiceType(
        @Header("Authorization") token: String
    ) : Response<ServiceTypeApi.ServiceTypeResponse>

    @FormUrlEncoded
    @POST("display_servicetype_ceneterwise")
    suspend fun userServiceTypeCenterWise(
        @Header("Authorization") token: String,
        @Field("servicecenter_id") serviceCenterId: String
    ) : Response<ServiceTypeApi.ServiceTypeResponse>

    @FormUrlEncoded
    @POST("add_new_vehicle1")
    suspend fun userAddNewVehicle(
        @Header("Authorization") token: String,
        @Field("vehicle_regno") vehicle_regno: String,
        @Field("vehicle_chassis_no") vehicle_chassis_no: String,
        @Field("vehicle_model") vehicle_model: String,
        @Field("vehicle_reg_year") vehicle_reg_year: String
    ) : Response<AddNewVehicleApi.AddNewVehicleResponse>

    @FormUrlEncoded
    @POST("available_slots")
    suspend fun userAvailableSlot(
        @Header("Authorization") token: String,
        @Field("service_center") service_center: String,
        @Field("service_date") service_date: String?
    ) : Response<AvailableSlotApi.AvailableSlotResponse>

    @FormUrlEncoded
    @POST("appointment_booking")
    suspend fun userAppointmentBooking(
        @Header("Authorization") token: String,
        @Field("vehicle") vehicle: String,
        @Field("service_center") service_center: String,
        @Field("slot") slot: String,
        @Field("date") date: String,
        @Field("service_type") service_type: String,
        @Field("service") service: String,
        @Field("note") note: String
    ) : Response<AppointmentBookingApi.AppointmentBookingResponse>

    @POST("my_vehicles")
    suspend fun userMyVehicles(
        @Header("Authorization") token: String
    ) : Response<MyVehiclesApi.MyVehiclesResponse>

    @FormUrlEncoded
    @POST("vehicle_history")
    suspend fun userVehicleHistory(
        @Header("Authorization") token: String,
        @Field("vehicle") vehicle: String
    ) : Response<VehicleHistoryApi.VehicleHistoryResponse>

    @POST("notifications")
    suspend fun userNotifications(
        @Header("Authorization") token: String
    ) : Response<NotificationApi.NotificationResponse>

    @FormUrlEncoded
    @POST("my_bookings")
    suspend fun userMyBookings(
        @Header("Authorization") token: String,
        @Field("type") type: String?
    ) : Response<MyBookingsApi.MyBookingsResponse>

    @FormUrlEncoded
    @POST("slider")
    suspend fun userSliders(
        @Header("Authorization") token: String,
        @Field("stat") stat: String?
    ) : Response<SlidersApi.SlidersResponse>

    @FormUrlEncoded
    @POST("services")
    suspend fun userServices(
        @Header("Authorization") token: String,
        @Field("service_type") service_type: String
    ) : Response<ServicesApi.ServicesResponse>

    @POST("vehiclemodels")
    suspend fun userVehicleModelYear()
    : Response<VehicleModelApi.VehicleModelResponse>

    @FormUrlEncoded
    @POST("servicepricing")
    suspend fun userServicePricing(
        @Header("Authorization") token: String,
        @Field("model_id") model_id: String,
        @Field("servicetype_id") servicetype_id: String?
    ) : Response<ServicePricingApi.ServicePricingResponse>

    @FormUrlEncoded
    @POST("signuppic")
    suspend fun userSignUpVehicleImg(
        @Field("type") type: String?
    ) : Response<SignUpImageApi.SignUpImageResponse>

    @POST("myvehiclepic")
    suspend fun userMyVehicleImg(
        @Header("Authorization") token: String
    ) : Response<VehicleImageApi.VehicleImageResponse>

    @FormUrlEncoded
    @POST("reset_password")
    suspend fun userForgotPassword(
        @Field("email") name: String
    ) : Response<ForgotPasswordApi.ForgotPasswordResponse>

    @FormUrlEncoded
    @POST("update_customer")
    suspend fun userUpdateProfile(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String
    ) : Response<UpdateProfileApi.UpdateProfileResponse>

    @FormUrlEncoded
    @POST("update_password")
    suspend fun userPassword(
        @Header("Authorization") token: String,
        @Field("currpass") name: String,
        @Field("resetpass") phone: String,
        @Field("confirmpass") email: String
    ) : Response<UpdateProfileApi.UpdateProfileResponse>

    /*@FormUrlEncoded
    @POST("update_customer")
    suspend fun userUpdateProfile(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("phone2") phone2: String?,
        @Field("dob") dob: String?,
        @Field("gender") gender: String?,
        @Field("createpass") createpass: String,
        @Field("resetpass") confirmpass: String,
        @Field("email") email: String
    ) : Response<UpdateProfileApi.UpdateProfileResponse>*/

    @FormUrlEncoded
    @POST("cancel_booking")
    suspend fun userCancelBooking(
        @Header("Authorization") token: String,
        @Field("booking") booking: String
    ) : Response<CancelBookingApi.CancelBookingResponse>

    @FormUrlEncoded
    @POST("notification_android")
    suspend fun userFcmToken(
        @Header("Authorization") token: String,
        @Field("token") fcmToken: String
    ) : Response<FcmApi.FcmResponse>

    @FormUrlEncoded
    @POST("vehicle_model_year")
    suspend fun userVehicleModelYear(
        @Field("vehicle_regno") reg: String,
        @Field("vehicle_chassis_no") chassis: String
    ) : Response<VehicleModelYearApi.VehicleModelYearResponse>

    @FormUrlEncoded
    @POST("clear_notification")
    suspend fun userClearNotifications(
        @Header("Authorization") token: String,
        @Field("notification_id") id: String
    ) : Response<NotificationApi.NotificationReadClearResponse>

    @FormUrlEncoded
    @POST("read_notification")
    suspend fun userReadNotifications(
        @Header("Authorization") token: String,
        @Field("notification_id") id: String
    ) : Response<NotificationApi.NotificationReadClearResponse>

    @POST("contact")
    suspend fun userContactInfo(
        @Header("Authorization") token: String
    ) : Response<ContactApi.ContactResponse>

    @FormUrlEncoded
    @POST("Remove_my_vehicle")
    suspend fun userRemoveVehicle(
        @Header("Authorization") token: String,
        @Field("vehicle_id") id: String
    ) : Response<RemoveVehiclesApi.RemoveVehiclesResponse>

    @GET("accident_number")
    suspend fun getAccidentNumber(
        @Header("Authorization") token: String,
    ) : Response<AccidentApi.AccidentResponse>

    @FormUrlEncoded
    @POST("spair_parts_mail")
    suspend fun spairPartsMail(
        @Header("Authorization") token: String,
        @Field("subject") subject: String,
        @Field("enquery") enquiry: String
    ) : Response<AccidentApi.AccidentResponse>
    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : MyApi {

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .callTimeout(300, TimeUnit.SECONDS)
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(PRO_BASE_API_URL)
                //.baseUrl(TEST_BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}