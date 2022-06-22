package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class VehicleHistoryApi {

    data class VehicleHistoryResponse(

        @SerializedName("data")
        var data: VehicleHistoryData? = null

    )

    data class VehicleHistoryData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: MutableList<VehicleHistoryDataResponse>? = null

    )

    data class VehicleHistoryDataResponse(

        @SerializedName("docdate")
        var docdate: String? = null,

        @SerializedName("orderno")
        var orderno: String? = null,

        @SerializedName("docno")
        var docno: String? = null,

        @SerializedName("invoicevalue")
        var invoicevalue: String? = null,

        @SerializedName("description")
        var description: String? = null,

        @SerializedName("appoinments_slote")
        var appoinments_slote: String? = null,

        @SerializedName("servicecenter_location_address")
        var servicecenter_location_address: String? = null,

        @SerializedName("regno")
        var regno: String? = null,

        @SerializedName("chasis")
        var chasis: String? = null,

        @SerializedName("model")
        var model: String? = null,

        @SerializedName("modelyear")
        var modelyear: String? = null

    )

}