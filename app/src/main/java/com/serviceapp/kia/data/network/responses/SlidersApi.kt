package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class SlidersApi {

    data class SlidersResponse(

        @SerializedName("data")
        var data: SlidersData? = null

    )

    data class SlidersData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: MutableList<SlidersDataResponse>? = null

    )

    data class SlidersDataResponse(

        @SerializedName("slider_title")
        var slider_title: String? = null,

        @SerializedName("slider_pic")
        var slider_pic: String? = null

    )

}