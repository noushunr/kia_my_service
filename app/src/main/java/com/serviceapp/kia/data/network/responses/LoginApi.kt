package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 26-06-2021
*/

class LoginApi {

    data class LoginResponse(

        @SerializedName("data")
        var data: LoginData? = null

    )

    data class LoginData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: LoginDataResponse? = null

    )

    data class LoginDataResponse(

        @SerializedName("username")
        var username: String? = null,

        @SerializedName("usermail")
        var usermail: String? = null,

        @SerializedName("usermobile")
        var usermobile: String? = null,

        @SerializedName("token")
        var token: String? = null

    )

}