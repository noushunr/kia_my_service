package com.serviceapp.kia.data.network

import com.serviceapp.kia.utils.ApiException
import com.serviceapp.kia.utils.ErrorBodyException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

/*
 *Created by Adithya T Raj on 24-06-2021
*/

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            println(response.body().toString())
            return response.body()!!
        } else if(response.code() == 401 || response.code() == 400 || response.code() == 100) {
            val error = response.errorBody()?.use {
                it.string()
            }
            throw ErrorBodyException(error)
        } else {
            val error = response.errorBody()?.use {
                it.string()
            }

            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            throw ApiException(message.toString())
        }
    }

}