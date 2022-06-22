package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class MyBookingsApi {

    data class MyBookingsResponse(

        @SerializedName("data")
        var data: MyBookingsData? = null

    )

    data class MyBookingsData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: MutableList<MyBookingsDataResponse>? = null

    )

    data class MyBookingsDataResponse(

        @SerializedName("vehicle_regno")
        var vehicle_regno: String? = null,

        @SerializedName("appoinments_booking_no")
        var appoinments_booking_no: String? = null,

        @SerializedName("appoinments_slote")
        var appoinments_slote: String? = null,

        @SerializedName("appoinments_date")
        var appoinments_date: String? = null,

        @SerializedName("appoinments_date_taken")
        var appoinments_date_taken: String? = null,

        @SerializedName("appoinments_note")
        var appoinments_note: String? = null,

        @SerializedName("appoinment_status")
        var appoinment_status: String? = null,

        @SerializedName("service_name")
        var service_name: String? = null,

        @SerializedName("servicetype_name")
        var servicetype_name: String? = null,

        @SerializedName("servicecenter_location_address")
        var servicecenter_location_address: String? = null

    )

}