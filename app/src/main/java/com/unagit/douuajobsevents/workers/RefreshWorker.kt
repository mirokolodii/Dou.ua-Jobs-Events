package com.unagit.douuajobsevents.workers

import android.app.*
import android.content.Intent
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

    companion object {
        private const val NOTIFICATION_CHANNEL_NAME = "General"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "Notifications about new items"
        private const val NOTIFICATION_ID = 1

    }
    private var dataProvider: DataProvider? = null


    override fun doWork(): Result {
        initializeFields()

        refreshData()

        return Result.SUCCESS
    }

    // Get instance of DataProvider
    private fun initializeFields() {
        if (dataProvider == null) {
            dataProvider = DataProvider(applicationContext as Application)
        }
    }


    /**
     * Asks DataProvider to refresh data from web and shows notification,
     * informing about number of newly received items.
     */
    private fun refreshData() {
        dataProvider!!.getRefreshDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .blockingSubscribe(object : DisposableObserver<List<Item>>() {

                    override fun onComplete() {}

                    override fun onNext(t: List<Item>) {
                        showNotification(t.size)
                    }

                    override fun onError(e: Throwable) {
                        Log.e(
                                this@RefreshWorker.javaClass.simpleName,
                                e.message)
                    }
                })

    }

    /**
     * Shows notification with a number of newly received items.
     * @param itemsCount number of newly received items
     */
    private fun showNotification(itemsCount: Int) {
        // Don't show notification when no new items received
        if (itemsCount == 0) {
            return
        }
        val message = when(itemsCount) {
            1 -> "$itemsCount new item received."
            else -> "$itemsCount new items received."
        }

        val notificationManager: NotificationManager =
                this.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "channel_id"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NOTIFICATION_CHANNEL_NAME
            val descriptionText = NOTIFICATION_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register channel with the system
            notificationManager.createNotificationChannel(channel)
        }

        // Create a PendingIntent with MainActivity, which will be triggered on notification click
        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)
        val mainActivityPendingIntent =
                PendingIntent.getActivity(applicationContext, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Build notification
        val mBuilder = NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.abc_ic_menu_overflow_material)
                .setContentTitle(message)
//                .setSubText("subtext")
                .setContentText("Tap to open in app.")
//                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setColor(Color.GREEN)
                .setContentIntent(mainActivityPendingIntent)
                .setAutoCancel(true)

        // Send notification
        try {
            notificationManager.notify(NOTIFICATION_ID, mBuilder.build())
        } catch (e: NullPointerException) {
            Log.e(
                    this@RefreshWorker.javaClass.simpleName,
                    e.message)
        }

    }

}
