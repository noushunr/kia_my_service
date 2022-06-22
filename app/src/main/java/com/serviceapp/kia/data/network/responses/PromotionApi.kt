package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class PromotionApi {

    data class PromotionResponse(

        @SerializedName("data")
        var data: PromotionData? = null

    )

    data class PromotionData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: MutableList<PromotionDataResponse>? = null

    )

    data class PromotionDataResponse(

        @SerializedName("promotion_id")
        var promotion_id: String? = null,

        @SerializedName("promotion_name")
        var promotion_name: String? = null,

        @SerializedName("promotion_url")
        var promotion_url: String? = null,

        @SerializedName("promotion_date")
        var promotion_date: String? = null

    )

}