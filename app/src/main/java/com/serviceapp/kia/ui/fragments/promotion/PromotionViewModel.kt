package com.serviceapp.kia.ui.fragments.promotion

import androidx.lifecycle.ViewModel
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.PromotionApi
import com.serviceapp.kia.data.repositories.HomeRepository
import com.serviceapp.kia.utils.*

class PromotionViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    val appContext by lazy {
        repository.appContext
    }

    var promoUrl = ""

    var listener: NetworkListener? = null
    var errorMessage: String = ""

    fun fetchPromotions() {

        if (!hasNetwork()) {
            errorMessage = appContext.getLocaleStringResource(R.string.check_network)
            listener?.onFailure()
            return
        }

        listener?.onStarted()

        CoRoutines.main {
            try {
                val response = repository.userDisplayPromotion("Promotion")
                checkPromotionResponse(response)

            } catch (error: ErrorBodyException) {
                try {
                    val response : PromotionApi.PromotionResponse = fromJson(error.message.toString())

                    checkPromotionResponse(response)
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

    private fun checkPromotionResponse(response : PromotionApi.PromotionResponse) {

        val data = response.data
        val message = data?.message
        val status = data?.status
        val res = data?.response

        errorMessage = message.toString()

        if (data != null  && status.equals(SUCCESS_RESPONSE_CODE) && res != null) {
            if (res.size > 0) {
                promoUrl = res[0].promotion_url.toString()
                listener?.onSuccess()
            }
        } else {
            listener?.onFailure()
        }

        println(response.toString())
    }
}