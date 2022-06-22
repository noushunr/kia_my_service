package com.serviceapp.kia.ui.fragments.my.vehicle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.MyVehiclesApi
import com.serviceapp.kia.data.network.responses.RemoveVehiclesApi
import com.serviceapp.kia.data.network.responses.VehicleImageApi
import com.serviceapp.kia.data.repositories.HomeRepository
import com.serviceapp.kia.utils.*

class MyVehicleViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    var vehicleImgUrl = ""

    var isImg = false

    private val mutableVehicle = MutableLiveData<MutableList<MyVehiclesApi.MyVehiclesDataResponse>>()

    val liveVehicle : LiveData<MutableList<MyVehiclesApi.MyVehiclesDataResponse>>
        get() = mutableVehicle

    fun fetchVehicle() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        isImg = false

        CoRoutines.main {
            try {
                val response = repository.userMyVehicles()

                val data = response.data
                val message = data?.message
                val status = data?.status
                val res = data?.response

                errorMessage = message.toString()

                if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
                    if (res.size > 0) {
                        mutableVehicle.value = res!!
                    }else{
                        mutableVehicle.value = mutableListOf()
                    }
                    listener?.onSuccess()
                } else {
                    mutableVehicle.value = mutableListOf()
                    listener?.onFailure()
                }

                println(response.toString())

                fetchVehicleImg()

            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    private fun fetchVehicleImg() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        isImg = true

        CoRoutines.main {
            try {
                val response = repository.userMyVehicleImg()
                checkVehicleImgResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : VehicleImageApi.VehicleImageResponse = fromJson(error.message.toString())

                    checkVehicleImgResponse(response)
                } catch (e: Exception) {
                    errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    private fun checkVehicleImgResponse(response : VehicleImageApi.VehicleImageResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            if (res.myvehicle_pic != null) {
                vehicleImgUrl = res.myvehicle_pic!!
            }
        }
        listener?.onSuccess()

        println(response.toString())
    }

    fun removeVehicle(id: String) {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        isImg = false

        CoRoutines.main {
            try {
                val response = repository.userRemoveVehicle(id)
                checkRemoveVehicleResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : RemoveVehiclesApi.RemoveVehiclesResponse = fromJson(error.message.toString())

                    checkRemoveVehicleResponse(response)
                } catch (e: Exception) {
                    errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                    println(e.message)
                    listener?.onFailure()
                }
            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    private fun checkRemoveVehicleResponse(response : RemoveVehiclesApi.RemoveVehiclesResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            listener?.onSuccess()
        } else {
            listener?.onFailure()
        }

        println(response.toString())

        fetchVehicle()
    }
}