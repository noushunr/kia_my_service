package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class AddNewVehicleApi {

    data class AddNewVehicleResponse(

        @SerializedName("data")
        var data: AddNewVehicleData? = null

    )

    data class AddNewVehicleData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: AddNewVehicleDataResponse? = null

    )

    data class AddNewVehicleDataResponse(

        @SerializedName("Vehicle")
        var Vehicle: String? = null,

        @SerializedName("vehicle_id")
        var vehicle_id: String? = null

    )

}