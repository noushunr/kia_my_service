package com.serviceapp.kia.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.serviceapp.kia.utils.CURRENT_USER_ID
import com.google.gson.annotations.SerializedName

/*
 *Created by Adithya T Raj on 24-06-2021
*/

@Entity
data class User (

    @SerializedName("user_id")

    var user_id: String? = null,

    @SerializedName("unique_id")
    var unique_id: String? = null,

    @SerializedName("user_name")
    var user_name: String? = null,

    @SerializedName("user_email")
    var user_email: String? = null,

    @SerializedName("mobile")
    var mobile: String? = null,

    @SerializedName("verfied")
    var verfied: String? = null

){
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}