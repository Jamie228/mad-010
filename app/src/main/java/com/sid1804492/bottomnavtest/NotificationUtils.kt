package com.sid1804492.bottomnavtest

import com.sid1804492.bottomnavtest.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { NotificationHelper(it) }?.createNotification()
    }

}

class NotificationHelper(private val mContext: Context) {
    fun createNotification() {
        val intent = Intent(mContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val resultPendingIntent = NavDeepLinkBuilder(mContext)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.navigation_new_wellbeing)
            .createPendingIntent()
        val mBuilder =
            NotificationCompat.Builder(
                mContext,
                NOTIFICATION_CHANNEL_ID
            )
        mBuilder.setSmallIcon(R.drawable.ic_baseline_school_24)
        mBuilder.setContentTitle("Teacher Planner")
            .setContentText("Reflect on your teaching practice or planning?")
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
        val mNotificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            notificationChannel.vibrationPattern = longArrayOf(
                100,
                200,
                300,
                400,
                500,
                400,
                300,
                200,
                400
            )
            assert(mNotificationManager != null)
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
        assert(mNotificationManager != null)
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build())
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "10001"
    }

}