package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 27-06-2021
*/

class NotificationApi {

    data class NotificationResponse(

        @SerializedName("data")
        var data: NotificationData? = null

    )

    data class NotificationData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: MutableList<NotificationDataResponse>? = null

    )

    data class NotificationDataResponse(

        @SerializedName("notification_id")
        var id: String? = null,

        @SerializedName("title")
        var title: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("created_at")
        var created_at: String? = null,

        @SerializedName("readed_status")
        var readed_status: String? = null,

        @SerializedName("cleared_status")
        var cleared_status: String? = null

    )

    data class NotificationReadClearResponse(

        @SerializedName("data")
        var data: NotificationReadClearData? = null

    )

    data class NotificationReadClearData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: String? = null

    )

}