package com.serviceapp.kia.data.network.responses

import com.google.gson.annotations.SerializedName


/*
 *Created by Adithya T Raj on 20-02-2022
*/

class ContactApi {

    data class ContactResponse(

        @SerializedName("data")
        var data: ContactData? = null

    )

    data class ContactData(

        @SerializedName("status")
        var status: String? = null,

        @SerializedName("message")
        var message: String? = null,

        @SerializedName("response")
        var response: MutableList<ContactDataResponse>? = null

    )

    data class ContactDataResponse(

        @SerializedName("contact_id")
        var contact_id: String? = null,

        @SerializedName("contact_address")
        var contact_address: String? = null,

        @SerializedName("contact_phon")
        var contact_phon: String? = null,

        @SerializedName("contact_mail")
        var contact_mail: String? = null,

        @SerializedName("contact_location_url")
        var contact_location_url: String? = null,

        @SerializedName("contact_date")
        var contact_date: String? = null

    )

}