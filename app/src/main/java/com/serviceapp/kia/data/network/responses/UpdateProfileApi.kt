package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 11-07-2021
*/

class UpdateProfileApi {

    data class UpdateProfileResponse(

        @SerializedName("data")
        var data: UpdateProfileData? = null

    )

    data class UpdateProfileData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: UpdateProfileDataResponse? = null

    )

    data class UpdateProfileDataResponse(

        @SerializedName("customers_name")
        var customers_name: String? = null,

        @SerializedName("customers_phone")
        var customers_phone: String? = null,

        @SerializedName("customers_mail")
        var customers_mail: String? = null

    )

}