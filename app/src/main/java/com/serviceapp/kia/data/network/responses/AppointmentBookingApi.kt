package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class AppointmentBookingApi {

    data class AppointmentBookingResponse(

        @SerializedName("data")
        var data: AppointmentBookingData? = null

    )

    data class AppointmentBookingData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: AppointmentBookingDataResponse? = null

    )

    data class AppointmentBookingDataResponse(

        @SerializedName("appointment")
        var appointment: String? = null

    )

}