package com.serviceapp.kia.ui.fragments.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.NotificationApi
import com.serviceapp.kia.data.repositories.HomeRepository
import com.serviceapp.kia.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val appContext by lazy {
        repository.appContext
    }

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    private val mutableNotification = MutableLiveData<MutableList<NotificationApi.NotificationDataResponse>>()

    val liveNotification : LiveData<MutableList<NotificationApi.NotificationDataResponse>>
        get() = mutableNotification

    fun fetchNotification() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userNotifications()

                checkResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : NotificationApi.NotificationResponse = fromJson(error.message.toString())

                    checkResponse(response)
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

    private fun checkResponse(response : NotificationApi.NotificationResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        val newList : MutableList<NotificationApi.NotificationDataResponse> = mutableListOf()
        mutableNotification.value = mutableListOf()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            if (res.size > 0) {
                for (item in res) {
                    if (item.cleared_status != "1") {
                        newList.add(item)
                    }
                }
            }
            mutableNotification.value = newList
            listener?.onSuccess()
        } else {
            mutableNotification.value = mutableListOf()
            listener?.onFailure()
        }

        println(response.toString())

        for (item in newList) {
            if (item.readed_status.equals("0")) {
                CoRoutines.main {
                    readNotification(item.id.toString())
                    delay(100)
                }
            }
        }

    }

    private fun readNotification(id: String) {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        //listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userReadNotifications(id)

                checkReadClearResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : NotificationApi.NotificationReadClearResponse = fromJson(error.message.toString())

                    checkReadClearResponse(response)
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

    fun clearNotification(id: String) {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userClearNotifications(id)

                checkReadClearResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : NotificationApi.NotificationReadClearResponse = fromJson(error.message.toString())

                    checkReadClearResponse(response)
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

        fetchNotification()

    }

    private fun checkReadClearResponse(response : NotificationApi.NotificationReadClearResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE)) {
            listener?.onSuccess()
        } else {
            listener?.onFailure()
        }

        println(response.toString())

    }
}