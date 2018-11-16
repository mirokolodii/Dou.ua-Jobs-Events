package com.unagit.douuajobsevents.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.unagit.douuajobsevents.R

class RefreshAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
//        if (intent != null && intent.action == context?.getString(R.string.refresh_alarm_receiver_action)) {
            context?.startService(Intent(context, RefreshService::class.java))
//        }
    }

}