package com.unagit.douuajobsevents.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.unagit.douuajobsevents.R

class RefreshAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.startService(Intent(context, RefreshService::class.java))
        Log.d("alarmManager", "RefreshAlarmReceiver is triggered")
    }

}