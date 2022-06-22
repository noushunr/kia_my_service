package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class AvailableSlotApi {

    data class AvailableSlotResponse(

        @SerializedName("data")
        var data: AvailableSlotData? = null

    )

    data class AvailableSlotData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: String? = null

    )

}