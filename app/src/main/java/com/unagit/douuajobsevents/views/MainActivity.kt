package com.unagit.douuajobsevents.views

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.ListPresenter
import com.unagit.douuajobsevents.services.RefreshService
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Pair as AndroidPair
import android.app.AlarmManager
import androidx.core.content.ContextCompat.getSystemService
import android.app.PendingIntent
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import com.unagit.douuajobsevents.services.RefreshAlarmReceiver
import java.util.*
import android.view.MenuInflater
import android.view.MenuItem
import androidx.work.*
import com.unagit.douuajobsevents.helpers.WorkerConstants.UNIQUE_REFRESH_WORKER_NAME
import com.unagit.douuajobsevents.services.RefreshWorker
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), ListContract.ListView, ItemAdapter.OnClickListener {

    private val presenter: ListContract.ListPresenter = ListPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attach(this, application)
        presenter.getItems()

//        button.setOnClickListener {
//            presenter.refreshData()
//        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        scheduleRefreshWorkerTask()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_refresh -> {
                presenter.refreshData()
                true
            }
            R.id.menu_clear_cache -> {
                presenter.clearLocalData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun scheduleRefreshService() {
        val intent = Intent(this, RefreshAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis =System.currentTimeMillis()
//        calendar.add(Calendar.MINUTE, 5)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pendingIntent)
        Log.d("alarmManager", "AlarmManager is set from MainActivity")
    }

    private fun scheduleRefreshWorkerTask() {
        Log.d("WorkManager", "scheduleRefreshWorkerTask triggered.")
        val workConstraints = Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
//        val applicationData = Data.Builder()
//                .putString(Keys.APP_ID, getApplicationId())
//                .putString(Keys.LOGGING_LEVEL, mitterConfig.loggingLevel.toString())
//                .build()

//
        val periodicRefreshRequest = PeriodicWorkRequest.Builder(
                RefreshWorker::class.java,
                15,
                TimeUnit.MINUTES
        ).setConstraints(workConstraints)
//                .setInputData(applicationData)
                .build()
        WorkManager.getInstance()
                .enqueueUniquePeriodicWork(
                        UNIQUE_REFRESH_WORKER_NAME,
                        ExistingPeriodicWorkPolicy.REPLACE,
                        periodicRefreshRequest
                )


//        val singleRefreshRequest = OneTimeWorkRequest
//                .Builder(RefreshWorker::class.java)
//                .setConstraints(workConstraints)
////                .setInputData(applicationData)
//                .build()
//        WorkManager.getInstance()
//                .enqueue(singleRefreshRequest)

    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }


    override fun showItems(items: List<Item>) {

//                val listener = object : ItemAdapter.Listener {
//            override fun onItemClicked(position: Int) {
//                presenter.itemClicked(position)
//            }
//        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ItemAdapter(items.toMutableList(), this@MainActivity)
        }

    }

    override fun insertNewItems(newItems: List<Item>) {
        val adapter = recyclerView.adapter as ItemAdapter
        val insertPosition = 0
        adapter.insertData(newItems, insertPosition)
        recyclerView.scrollToPosition(insertPosition)
    }


    override fun onItemClicked(parent: View, guid: String) {
        // Prepare transition animation.
        val imgView = parent.findViewById<View>(R.id.itemImg)
        val titleView = parent.findViewById<View>(R.id.itemTitle)
        val transImgName = getString(R.string.transition_img_name)
        val transTitleName = getString(R.string.transition_title_name)
        val transContainerName = getString(R.string.transition_container_name)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this@MainActivity,
                AndroidPair.create(imgView, transImgName),
                AndroidPair.create(titleView, transTitleName),
                AndroidPair.create(parent, transContainerName)
        )

        // Start new DetailsActivity with transition animation and pass guid to it.
        val detailsIntent = Intent(this@MainActivity, DetailsActivity::class.java)
        detailsIntent.putExtra(getString(R.string.extra_guid_id), guid)
        startActivity(detailsIntent, transitionActivityOptions.toBundle())
    }

    override fun showSnackbar(string: String) {
        Snackbar.make(activityMainLayout, string, Snackbar.LENGTH_SHORT).show()
    }

    override fun hasNetwork(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return (activeNetwork != null && activeNetwork.isConnected)
    }
}
