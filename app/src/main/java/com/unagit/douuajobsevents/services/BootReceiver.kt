package com.unagit.douuajobsevents.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
        scheduleRefreshService(context)
        }
    }

    private fun scheduleRefreshService(context: Context?) {
        val intent = Intent(context, RefreshAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis =System.currentTimeMillis()
//        calendar.add(Calendar.MINUTE, 5)

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pendingIntent)
    }

}