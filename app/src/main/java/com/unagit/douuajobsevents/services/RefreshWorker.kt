package com.unagit.douuajobsevents.services

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.unagit.douuajobsevents.models.DataProvider
import com.unagit.douuajobsevents.models.Item
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import androidx.core.app.NotificationCompat
import com.unagit.douuajobsevents.views.MainActivity
import android.content.Context
import android.os.Build
import androidx.annotation.NonNull
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.unagit.douuajobsevents.R


class RefreshWorker(@NonNull appContext: Context,
                    @NonNull workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    private var dataProvider: DataProvider? = null
    private val notificationId = 1
    private val logTag = this.javaClass.simpleName



    override fun doWork(): Result {
        Log.d("alarmManager", "$logTag service started")
        initializeFields()


        refreshData()

        Log.d(logTag, "About to return Result.SUCCESS.")
        return Result.SUCCESS
    }

    private fun initializeFields() {
        if (dataProvider == null) {
            dataProvider = DataProvider(applicationContext as Application)
        }
    }


    private fun refreshData() {
        Log.d(logTag, "refreshData triggered")
        dataProvider!!.getRefreshDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .blockingSubscribe(object : DisposableObserver<List<Item>>() {

                    override fun onComplete() {
                        Log.d(logTag, "onComplete triggered.")
                    }

                    override fun onNext(t: List<Item>) {
                        Log.d(logTag, "onNext triggered.")
                        showNotification(t.size)
                    }

                    override fun onError(e: Throwable) {
                        Log.d(logTag, "onError triggered ${e.printStackTrace()}.")
                    }
                })

    }

    private fun showNotification(itemsNumber: Int) {

        Log.d(logTag, "showNotification triggered.")

        val notificationManager: NotificationManager =
                this.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "channel_id"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "notification channel name"
            val descriptionText = "notification channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }

        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)
        val mainActivityPendingIntent =
                PendingIntent.getActivity(applicationContext, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val mBuilder = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.abc_ic_menu_overflow_material)
                .setContentTitle("$itemsNumber new item(s) received")
//                .setSubText("subtext")
                .setContentText("Tap to open in app.")
//                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setColor(Color.GREEN)
                .setContentIntent(mainActivityPendingIntent)
                .setAutoCancel(true)

        // Send notification
        try {
            notificationManager.notify(notificationId, mBuilder.build())
        } catch (e: NullPointerException) {
            Log.e("Refresh service notif.", "NotificationManager.notify throws an exception: " + e.message)
        }

    }

}
