package com.unagit.douuajobsevents.views

import android.app.ActivityOptions
import android.app.SearchManager
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
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Pair as AndroidPair
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.work.*
import com.unagit.douuajobsevents.helpers.WorkerConstants.UNIQUE_REFRESH_WORKER_NAME
import com.unagit.douuajobsevents.workers.RefreshWorker
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), ListContract.ListView, ItemAdapter.OnClickListener {

    private val presenter: ListContract.ListPresenter = ListPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Search", "onCreate triggered.")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attach(this, application)
        presenter.getItems()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        scheduleRefreshWorkerTask()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenuItem = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        searchMenuItem.apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
//            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d("Search", "onQueryTextChange triggered.")
                    val adapter = recyclerView.adapter as ItemAdapter

                    // Filter results based on search query.
                    adapter.filter.filter(newText)

                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d("Search", "onQueryTextSubmit triggered.")
                    return false
                }
            })

            setOnCloseListener {
                Log.d("Search", "onCloseListener triggered.")
                true
            }

            setOnQueryTextFocusChangeListener { v, hasFocus ->
                Log.d("Search", "setOnQueryTextFocusChangeListener hasFocus: $hasFocus.")

            }

        }



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

    private fun scheduleRefreshWorkerTask() {
        Log.d("WorkManager", "scheduleRefreshWorkerTask triggered.")
        val workConstraints = Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val periodicRefreshRequest = PeriodicWorkRequest
                .Builder(
                        RefreshWorker::class.java,
                        8,
                        TimeUnit.HOURS,
                        15,
                        TimeUnit.MINUTES)
                .setConstraints(workConstraints)
                .build()
        WorkManager.getInstance()
                .enqueueUniquePeriodicWork(
                        UNIQUE_REFRESH_WORKER_NAME,
                        ExistingPeriodicWorkPolicy.KEEP,
                        periodicRefreshRequest
                )
    }


    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun showItems(items: List<Item>) {
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

    override fun onNewIntent(intent: Intent?) {
        Log.d("Search", "onNewIntent triggered.")
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent?.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            performSearch(query)
        }
    }

    private fun performSearch(query: String) {
        Log.d("Search", "performSearch triggered.")
        showSnackbar(query)
    }
}
