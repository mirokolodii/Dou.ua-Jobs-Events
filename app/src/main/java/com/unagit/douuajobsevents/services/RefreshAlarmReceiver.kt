package com.unagit.douuajobsevents.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RefreshAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, RefreshService::class.java)
        context?.startService(serviceIntent)
    }

}