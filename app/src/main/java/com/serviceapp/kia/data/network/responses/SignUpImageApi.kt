package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 11-07-2021
*/

class SignUpImageApi {

    data class SignUpImageResponse(

        @SerializedName("data")
        var data: SignUpImageData? = null

    )

    data class SignUpImageData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: SignUpImageDataResponse? = null

    )

    data class SignUpImageDataResponse(

        @SerializedName("signupic_title")
        var signupic_title: String? = null,

        @SerializedName("signup_pic")
        var signup_pic: String? = null

    )

}