package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 11-07-2021
*/

class ForgotPasswordApi {

    data class ForgotPasswordResponse(

        @SerializedName("data")
        var data: ForgotPasswordData? = null

    )

    data class ForgotPasswordData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: String? = null

    )

}