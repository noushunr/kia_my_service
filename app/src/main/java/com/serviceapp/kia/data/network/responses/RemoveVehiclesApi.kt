package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 20-02-2022
*/

class RemoveVehiclesApi {

    data class RemoveVehiclesResponse(

        @SerializedName("data")
        var data: RemoveVehiclesData? = null

    )

    data class RemoveVehiclesData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: RemoveVehiclesDataResponse? = null

    )

    data class RemoveVehiclesDataResponse(

        @SerializedName("message")
        var message: String? = null

    )

}