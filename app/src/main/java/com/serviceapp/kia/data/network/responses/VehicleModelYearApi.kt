package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class VehicleModelYearApi {

    data class VehicleModelYearResponse(

        @SerializedName("data")
        var data: VehicleModelYearData? = null

    )

    data class VehicleModelYearData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: VehicleModelYearDataResponse? = null

    )

    data class VehicleModelYearDataResponse(

        @SerializedName("regno")
        var vehicle_regno: String? = null,

        @SerializedName("chassis")
        var vehicle_chassis_no: String? = null,

        @SerializedName("vehiclemodel_id")
        var vehicle_model_id: String? = null,

        @SerializedName("model")
        var vehicle_model: String? = null,

        @SerializedName("modelyear")
        var vehicle_reg_year: String? = null,

        @SerializedName("Vehicle")
        var Vehicle: String? = null

    )

}