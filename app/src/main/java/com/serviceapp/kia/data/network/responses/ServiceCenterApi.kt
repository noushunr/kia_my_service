package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class ServiceCenterApi {

    data class ServiceCenterResponse(

        @SerializedName("data")
        var data: ServiceCenterData? = null

    )

    data class ServiceCenterData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: MutableList<ServiceCenterDataResponse>? = null

    )

    data class ServiceCenterDataResponse(

        @SerializedName("servicecenter_id")
        var servicecenter_id: String,

        @SerializedName("servicecenter_name")
        var servicecenter_name: String? = null,

        @SerializedName("servicecenter_name_ar")
        var servicecenter_name_ar: String? = null,

        @SerializedName("servicecenter_address")
        var servicecenter_address: String? = null,

        @SerializedName("servicecenter_location_address")
        var servicecenter_location_address: String? = null,

        @SerializedName("servicecenter_location_area")
        var servicecenter_location_area: String? = null,

        @SerializedName("servicecenter_location_city")
        var servicecenter_location_city: String? = null,

        @SerializedName("servicecenter_location_lat")
        var servicecenter_location_lat: String = "0",

        @SerializedName("servicecenter_location_long")
        var servicecenter_location_long: String = "0",

        @SerializedName("servicecenter_date")
        var servicecenter_date: String? = null

    )

}