package com.unagit.douuajobsevents.views

import android.app.ActivityOptions
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.ScheduleRefreshWorker
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.helpers.WorkerConstants.UNIQUE_REFRESH_WORKER_NAME
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.ListPresenter
import com.unagit.douuajobsevents.workers.RefreshWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import android.util.Pair as AndroidPair


class MainActivity : BaseActivity(), ListContract.ListView, ItemAdapter.OnClickListener {

    private val presenter: ListContract.ListPresenter = ListPresenter()
    private val refresher: ListContract.Refresher = ScheduleRefreshWorker()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Search", "onCreate triggered.")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attach(this)

        // Request for items
        presenter.getItems()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initiate regular refreshment in background with Worker
        // (works even when app is closed)
        refresher.scheduleRefresh()
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
                    Log.d("Search", "onQueryTextChange triggered with text = $newText. ${newText.isNullOrEmpty()}")
                    val adapter = recyclerView.adapter as ItemAdapter

                    // Filter results based on search query.
                    adapter.filter.filter(newText)

                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
            })
        }



        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_refresh -> {
                // User's initiated data refreshment
                presenter.refreshData()
                true
            }
            R.id.menu_clear_cache -> {
                // User's initiated local data cleanup
                presenter.clearLocalData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    /**
     * Shows and hides spinner.
     * @param show true to show, false to hide
     */
    override fun showLoading(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    /**
     * Shows a list of received items from on the screen.
     * @param items to be shown
     */
    override fun showItems(items: List<Item>) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ItemAdapter(items.toMutableList(), this@MainActivity)
        }
    }

    /**
     * Inserts new items to the list.
     * @param newItems to be inserted
     */
    override fun insertNewItems(newItems: List<Item>) {
        val adapter = recyclerView.adapter as ItemAdapter
        val insertPosition = 0
        adapter.insertData(newItems, insertPosition)
        recyclerView.scrollToPosition(insertPosition)
    }

    /**
     * Callback method, triggered from ItemAdapter once user clicked on an item from the list.
     * Initiates a transition animation and passes guid of clicked item to DetailsActivity via intent.
     * @param parent view of an item, from which other sub-views are received for animation needs.
     * @param guid ID of an Item to be shown in DetailsActivity.
     * @see Item
     * @see DetailsActivity
     * @see ItemAdapter
     */
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

        // Start new DetailsActivity with transition animation and pass guid to it
        val detailsIntent = Intent(this@MainActivity, DetailsActivity::class.java)
        detailsIntent.putExtra(getString(R.string.extra_guid_id), guid)
        startActivity(detailsIntent, transitionActivityOptions.toBundle())
    }

    /**
     * Informs whether network connection is available.
     * @return Boolean. True - has network, false - no network
     */
    override fun hasNetwork(): Boolean {
        // TODO It's maybe worth to move this check to Application,
        // TODO and make it subscription (BroadcastReceiver) instead of simple getter.
        // TODO this way, you always gonna know, if your app is online, without additional calls
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
        showMessage(query)
    }
}
