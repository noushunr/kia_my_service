package com.serviceapp.kia.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import android.os.Build.VERSION.SDK_INT
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.serviceapp.kia.MainApplication
import com.serviceapp.kia.R
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


/*
 *Created by Adithya T Raj on 24-06-2021
*/

object CoRoutines {

    fun main(work: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }

    fun io(work: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.IO).launch {
            work()
        }

}

@Throws(JsonSyntaxException::class)
inline fun <reified T> fromJson(json: String): T {
    return Gson().fromJson(json, object: TypeToken<T>(){}.type)
}

fun hasNetwork(): Boolean {
    val context = MainApplication.appContext
    var isConnected = false // Initial Value
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        connectivityManager?.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                isConnected = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }
    } else {
        val activeNetwork: NetworkInfo? = connectivityManager?.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
    }
    return isConnected
}

@SuppressLint("DefaultLocale")
fun getDeviceName(): String =
    (if (MODEL.startsWith(MANUFACTURER, ignoreCase = true)) {
        "$MODEL (API $SDK_INT)"
    } else {
        "$MANUFACTURER $MODEL (API $SDK_INT)"
    }).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun applyImageUrl(builder: NotificationCompat.Builder, imageUrl: String) = runBlocking {

    withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val input = url.openStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            null
        }
    }?.let { bitmap ->
        builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
    }
}

fun getImageBitMap(encodedString: String) : Bitmap? {
    try {
        val byteArray = Base64.decode(encodedString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    } catch (e: Exception) {
        return null
    }
}

fun setYearMonthDateTimeStamp(date: String): Date? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    var newDate :Date? = null
    try {
        newDate = dateFormat.parse(date)
    } catch (e: Exception) {
        ////println(e.message)
    }
    return newDate
}

fun setMonthDateYear(date: String): String {
    var formattedString = date
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var newDate :Date? = null
    try {
        newDate = dateFormat.parse(date)
        formattedString = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(newDate!!)
    } catch (e: Exception) {
        ////println(e.message)
    }
    return formattedString
}

fun getFormattedDate(date: String): String {
    var formattedString = date
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var newDate :Date? = null
    try {
        newDate = dateFormat.parse(date)
        formattedString = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault()).format(newDate!!)
    } catch (e: Exception) {
        ////println(e.message)
    }
    return formattedString
}

fun getTime(date: Date): String =
    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)

fun getFormattedDate(date: Date): String =
    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)

fun getRelativeTop(myView: View): Int {
    return if (myView.parent === myView.rootView) {
        myView.top
    } else {
        myView.top + getRelativeTop(myView.parent as View)
    }
}

enum class ScaleTypes(value: String) {
    FIT("fit"), CENTER_CROP("centerCrop"), CENTER_INSIDE("centerInside")
}

fun setDateMonthYear(date: String): String {
    var formattedString = date
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    var newDate :Date? = null
    try {
        newDate = dateFormat.parse(date)
        formattedString = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(newDate!!)
    } catch (e: Exception) {
        ////println(e.message)
    }
    return formattedString
}

fun loadCarImage(view: AppCompatImageView, url: String?, drawable :Int = R.drawable.car_login_page) {
    Glide.with(view)
        .load(url)
        .into(view)

    view.visibility = View.VISIBLE
}