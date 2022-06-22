package com.serviceapp.kia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.serviceapp.kia.MainApplication
import com.serviceapp.kia.R
import com.serviceapp.kia.data.preferences.PreferenceProvider
import com.serviceapp.kia.utils.applyImageUrl
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/*
 *Created by Adithya T Raj on 25-07-2021
*/

class MyFireBaseMessagingService : FirebaseMessagingService() {

    private val prefs by lazy {
        PreferenceProvider(MainApplication.appContext)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.data.isNotEmpty()) {
            sendDataNotification(remoteMessage)
            println("Message data payload: " + remoteMessage.data)
        } else if (remoteMessage.notification != null) {
            sendNotification(remoteMessage)
            println("Message Notification Body: ${remoteMessage.notification?.body}")
        }
    }

    override fun onNewToken(token: String) {
        prefs.saveFireBaseDeviceToken(token)
    }

    private fun sendDataNotification(messageBody: RemoteMessage) {
        val channelId = "test_channel_id"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.app_icon_white)
            .setContentTitle(messageBody.data["title"].toString())
            .setContentText(messageBody.data["body"].toString())
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(applicationContext, R.color.darkBlue))

        val notificationBuilderImage = if (!messageBody.data["img"].isNullOrEmpty()) {
            applyImageUrl(notificationBuilder, messageBody.data["img"].toString())
        } else {
            null
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Myservice – Kia Al Mutawa",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        if (notificationBuilderImage != null) {
            notificationManager.notify(23 , notificationBuilderImage.build())
        } else {
            notificationManager.notify(23 , notificationBuilder.build())
        }
    }

    private fun sendNotification(messageBody: RemoteMessage) {
        val channelId = "test_channel_id"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.app_icon_white)
            .setContentTitle(messageBody.notification?.title)
            .setContentText(messageBody.notification?.body)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(applicationContext, R.color.darkBlue))

        val img = messageBody.notification?.imageUrl

        val notificationBuilderImage = if (img != null) {
            applyImageUrl(notificationBuilder, img.toString())
        } else {
            null
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Myservice – Kia Al Mutawa",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        if (notificationBuilderImage != null) {
            notificationManager.notify(23 , notificationBuilderImage.build())
        } else {
            notificationManager.notify(23 , notificationBuilder.build())
        }
    }

}