package com.serviceapp.kia.ui.fragments.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.MyVehiclesApi
import com.serviceapp.kia.data.network.responses.VehicleHistoryApi
import com.serviceapp.kia.data.repositories.HomeRepository
import com.serviceapp.kia.utils.*

class VehicleHistoryViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    var isHistory = false

    private val mutableVehicle = MutableLiveData<MutableList<MyVehiclesApi.MyVehiclesDataResponse>>()
    private val mutableVehicleHistory = MutableLiveData<MutableList<VehicleHistoryApi.VehicleHistoryDataResponse>>()

    val liveVehicle : LiveData<MutableList<MyVehiclesApi.MyVehiclesDataResponse>>
        get() = mutableVehicle
    val liveVehicleHistory : LiveData<MutableList<VehicleHistoryApi.VehicleHistoryDataResponse>>
        get() = mutableVehicleHistory

    fun fetchVehicle() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        isHistory = false

        listener?.onStarted()

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
                    }
                    listener?.onSuccess()
                } else {
                    listener?.onFailure()
                }

                println(response.toString())

            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

    fun fetchVehicleHistory(vehicleId: String) {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        isHistory = true

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userVehicleHistory(vehicleId)

                val data = response.data
                val message = data?.message
                val status = data?.status
                val res = data?.response

                errorMessage = message.toString()

                if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) ) {
                    if (res!=null && res.size > 0) {
                        mutableVehicleHistory.value = res!!

                    } else {
                        mutableVehicleHistory.value = mutableListOf()
//                        listener?.onFailure()
                    }
                    listener?.onSuccess()
                } else {
                    mutableVehicleHistory.value = mutableListOf()
                    listener?.onFailure()
                }

                println(response.toString())

            } catch (e: Exception) {
                errorMessage = appContext.getLocaleStringResource(R.string.something_wrong)
                println(e.message)
                listener?.onFailure()
            }
        }

    }

}