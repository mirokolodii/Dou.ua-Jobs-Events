package com.unagit.douuajobsevents

import androidx.work.*
import com.unagit.douuajobsevents.helpers.WorkerConstants
import com.unagit.douuajobsevents.ui.list.ListContract
import com.unagit.douuajobsevents.workers.RefreshWorker
import java.util.concurrent.TimeUnit


/**
 * Schedules a regular RefreshWorker task with WorkManager.
 * Triggered only with network connection available
 * during last hours of each 8 hours interval.
 * @see RefreshWorker
 */
class RefreshManager : ListContract.Refresher {
    override fun scheduleRefresh() {
        // We want worker to run only with network connection available
        val workConstraints = Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        // Set request with time and interval
        val periodicRefreshRequest = PeriodicWorkRequest
                .Builder(
                        RefreshWorker::class.java,
                        8,
                        TimeUnit.HOURS,
                        1,
                        TimeUnit.HOURS)
                .setConstraints(workConstraints)
                .build()

        // Enqueue unique worker
        WorkManager.getInstance()
                .enqueueUniquePeriodicWork(
                        WorkerConstants.UNIQUE_REFRESH_WORKER_NAME,
                        ExistingPeriodicWorkPolicy.KEEP,
                        periodicRefreshRequest
                )
    }

    /**
     * Haven't been used (and tested) before.
     * Cancels all work of RefreshWorker task.
     */
    override fun stopRefresh() {
        WorkManager.getInstance().cancelUniqueWork(WorkerConstants.UNIQUE_REFRESH_WORKER_NAME)
    }
}