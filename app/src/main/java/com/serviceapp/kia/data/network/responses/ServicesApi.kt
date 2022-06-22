package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class ServicesApi {

    data class ServicesResponse(

        @SerializedName("data")
        var data: ServicesData? = null

    )

    data class ServicesData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: MutableList<ServicesDataResponse>? = null,

        @SerializedName("image_url")
        var image_url: String? = null

    )

    data class ServicesDataResponse(

        @SerializedName("service_id")
        var service_id: String? = null,

        @SerializedName("service_name")
        var service_name: String? = null,

        @SerializedName("service_name_arab")
        var service_name_arab: String? = null,

        @SerializedName("service_price")
        var service_price: String? = null,

        @SerializedName("service_type")
        var service_type: String? = null,

        @SerializedName("service_image")
        var service_image: String? = null,

        @SerializedName("service_isdeleted")
        var service_isdeleted: String? = null,

        @SerializedName("service_date")
        var service_date: String? = null,

        @SerializedName("isChecked")
        var isChecked: Boolean = false

    )

}