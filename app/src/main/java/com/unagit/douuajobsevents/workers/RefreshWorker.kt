package com.unagit.douuajobsevents.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.unagit.douuajobsevents.MyApp
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.data.DataProvider
import com.unagit.douuajobsevents.helpers.Messages
import com.unagit.douuajobsevents.model.Item
import com.unagit.douuajobsevents.ui.list.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RefreshWorker(@NonNull val appContext: Context,
                    @NonNull workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    private val logTag = this.javaClass.simpleName
    @Inject
    lateinit var dataProvider: DataProvider
//    private var dataProvider: DataProvider? = null

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "channel_id"
        private const val NOTIFICATION_CHANNEL_NAME = "General"
        private const val NOTIFICATION_CHANNEL_DESCRIPTION = "Notifications about new items"
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_ICON_ID = R.drawable.ic_action_refresh
        private const val NOTIFICATION_CONTENT_TEXT = "Tap to open in app"
    }

    override fun doWork(): Result {
        (applicationContext as MyApp).appComponent.inject(this)
//        initializeFields()
        refreshData()
        return Result.success()
    }

    // Get instance of DataProvider
//    private fun initializeFields() {
//        if (dataProvider == null) {
//            dataProvider = MyApp.dataProvider
//        }
//    }

    /**
     * Asks DataProvider to refresh data from web and shows notification,
     * informing about number of newly received items.
     * Synchronous subscription is used instead of async, as we need to return
     * from worker only after refresh is done.
     */
    private fun refreshData() {
        val newItems = mutableListOf<Item>()
        dataProvider.getRefreshDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .blockingSubscribe(object : DisposableObserver<List<Item>>() {

                    override fun onComplete() {
                        showNotification(newItems.size)
                    }

                    override fun onNext(t: List<Item>) {
                        newItems.addAll(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.e(
                                logTag,
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
        val message = Messages.getMessageForCount(itemsCount)

        val notificationManager: NotificationManager =
                this.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = NOTIFICATION_CHANNEL_ID
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
        val mainActivityIntent = Intent(appContext, MainActivity::class.java)
        val mainActivityPendingIntent =
                PendingIntent.getActivity(appContext, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Build notification
        val mBuilder = NotificationCompat.Builder(appContext, channelId)
                .setSmallIcon(NOTIFICATION_ICON_ID)
                .setContentTitle(message)
//                .setSubText("subtext")
                .setContentText(NOTIFICATION_CONTENT_TEXT)
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
