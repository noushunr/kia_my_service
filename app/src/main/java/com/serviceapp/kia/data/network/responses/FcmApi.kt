package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class FcmApi {

    data class FcmResponse(

        @SerializedName("data")
        var data: FcmData? = null

    )

    data class FcmData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: FcmDataResponse? = null

    )

    data class FcmDataResponse(

        @SerializedName("token_android")
        var token_android: String? = null,

        @SerializedName("customer_id")
        var customer_id: String? = null,

        @SerializedName("token_time")
        var token_time: String? = null

    )

}