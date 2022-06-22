package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class ServiceTypeApi {

    data class ServiceTypeResponse(

        @SerializedName("data")
        var data: ServiceTypeData? = null

    )

    data class ServiceTypeData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: MutableList<ServiceTypeDataResponse>? = null

    )

    data class ServiceTypeDataResponse(

        @SerializedName("servicetype_id")
        var servicetype_id: String? = null,

        @SerializedName("servicetype_name")
        var servicetype_name: String? = null,

        @SerializedName("servicetype_name_arab")
        var servicetype_name_arab: String? = null,

        @SerializedName("servicetype_image")
        var servicetype_image: String? = null,

        @SerializedName("servicetype_isdeleted")
        var servicetype_isdeleted: String? = null,

        @SerializedName("servicetype_date")
        var servicetype_date: String? = null

    )

}