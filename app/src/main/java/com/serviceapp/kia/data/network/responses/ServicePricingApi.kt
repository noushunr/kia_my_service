package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 05-07-2021
*/

class ServicePricingApi {

    data class ServicePricingResponse(

        @SerializedName("data")
        var data: ServicePricingData? = null

    )

    data class ServicePricingData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: ServicePricingDataResponse? = null

    )

    data class ServicePricingDataResponse(

        @SerializedName("pdf_url")
        var pdf_url: String? = null

    )

}