package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class MyVehiclesApi {

    data class MyVehiclesResponse(

        @SerializedName("data")
        var data: MyVehiclesData? = null

    )

    data class MyVehiclesData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: MutableList<MyVehiclesDataResponse>? = null

    )

    data class MyVehiclesDataResponse(

        @SerializedName("vehicle_id")
        var vehicle_id: String? = null,

        @SerializedName("vehicle_regno")
        var vehicle_regno: String? = null,

        @SerializedName("vehicle_chassis_no")
        var vehicle_chassis_no: String? = null,

        @SerializedName("vehicle_model")
        var vehicle_model: String? = null,

        @SerializedName("vehicle_reg_year")
        var vehicle_reg_year: String? = null,

        @SerializedName("added_on")
        var added_on: String? = null,

        @SerializedName("due_date")
        var due_date: String? = null,

        @SerializedName("due_mileage")
        var due_mileage: String? = null,

        @SerializedName("vehicle_model_arab")
        var vehicle_model_arab: String? = null

    )

}