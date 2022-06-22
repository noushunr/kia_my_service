package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 20-02-2022
*/

class AccidentApi {

    data class AccidentResponse(

        @SerializedName("data")
        var data: AccidentData? = null

    )

    data class AccidentData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: AccidentDataResponse? = null

    )

    data class AccidentDataResponse(

        @SerializedName("accidentnumber_id")
        var accidentnumber_id: String? = null,

        @SerializedName("accidentnumber_number")
        var accidentnumber_number: String? = null,

        @SerializedName("accidentnumber_date")
        var accidentnumber_date: String? = null,


    )

}