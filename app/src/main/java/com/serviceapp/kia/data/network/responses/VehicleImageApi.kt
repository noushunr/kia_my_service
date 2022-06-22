package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 11-07-2021
*/

class VehicleImageApi {

    data class VehicleImageResponse(

        @SerializedName("data")
        var data: VehicleImageData? = null

    )

    data class VehicleImageData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: VehicleImageDataResponse? = null

    )

    data class VehicleImageDataResponse(

        @SerializedName("vehicleimage_title")
        var vehicleimage_title: String? = null,

        @SerializedName("myvehicle_pic")
        var myvehicle_pic: String? = null

    )

}