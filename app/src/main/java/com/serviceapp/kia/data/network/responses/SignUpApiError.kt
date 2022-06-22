package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class SignUpApiError {

    data class SignUpResponse(

        @SerializedName("data")
        var data: SignUpData? = null

    )

    data class SignUpData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

//        @SerializedName("response")
//        var response: SignUpDataResponse? = null

    )

    data class SignUpDataResponse(

        @SerializedName("cid")
        var cid: String? = null,

        @SerializedName("vehicle")
        var vehicle: String? = null

    )

}