package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class VehicleModelApi {

    data class VehicleModelResponse(

        @SerializedName("data")
        var data: VehicleModelData? = null

    )

    data class VehicleModelData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: MutableList<VehicleModelDataResponse>? = null

    )

    data class VehicleModelDataResponse(

        @SerializedName("vehiclemodel_id")
        var vehiclemodel_id: String? = null,

        @SerializedName("vehiclemodel_name")
        var vehiclemodel_name: String? = null,

        @SerializedName("vehiclemodel_name_arab")
        var vehiclemodel_name_arab: String? = null,

        @SerializedName("vehiclemodel_year")
        var vehiclemodel_year: String? = null

    )

}