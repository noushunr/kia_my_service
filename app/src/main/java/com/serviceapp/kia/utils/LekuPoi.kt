package com.serviceapp.kia.utils

import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import com.serviceapp.kia.data.network.responses.ServiceCenterApi

/*
 *Created by Adithya T Raj on 04-07-2021
*/

class LekuPoi : Parcelable {
    var id: String
    var location: Location
    var title: String
    var address: String = ""
    var center: ServiceCenterApi.ServiceCenterDataResponse? = null

    constructor(id: String, title: String, location: Location, center: ServiceCenterApi.ServiceCenterDataResponse, address: String) {
        this.id = id
        this.location = location
        this.title = title
        this.center = center
        this.address = address
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.id)
        dest.writeParcelable(this.location, flags)
        dest.writeString(this.title)
        dest.writeString(this.address)
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readString()!!
        this.location = `in`.readParcelable(Location::class.java.classLoader)!!
        this.title = `in`.readString()!!
        this.address = `in`.readString()!!
    }

    override fun toString(): String {
        return "LekuPoi{" + "id='" + id + '\''.toString() + ", location=" + location + ", title='" +
                title + '\''.toString() + ", address='" + address + '\''.toString() + '}'.toString()
    }

    companion object {

        @JvmField val CREATOR: Parcelable.Creator<LekuPoi> = object : Parcelable.Creator<LekuPoi> {
            override fun createFromParcel(source: Parcel): LekuPoi {
                return LekuPoi(source)
            }

            override fun newArray(size: Int): Array<LekuPoi?> {
                return arrayOfNulls(size)
            }
        }
    }
}