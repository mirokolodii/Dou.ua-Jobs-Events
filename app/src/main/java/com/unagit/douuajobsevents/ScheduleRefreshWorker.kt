package com.unagit.douuajobsevents

import androidx.work.*
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.workers.RefreshWorker
import androidx.work.WorkManager
import com.unagit.douuajobsevents.helpers.WorkerConstants
import java.util.concurrent.TimeUnit


/**
 * Schedules a regular RefreshWorker task with help of WorkManager.
 * Triggered only with network connection available
 * during last 15 minutes of each 8 hours interval.
 * @see RefreshWorker
 */
class ScheduleRefreshWorker : ListContract.Refresher {
    override fun scheduleRefresh() {
        // We want worker to run only with network connection available
        val workConstraints = Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val periodicRefreshRequest = PeriodicWorkRequest
                .Builder(
                        RefreshWorker::class.java,
                        8,
                        TimeUnit.HOURS,
                        1,
                        TimeUnit.HOURS)
                .setConstraints(workConstraints)
                .build()
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