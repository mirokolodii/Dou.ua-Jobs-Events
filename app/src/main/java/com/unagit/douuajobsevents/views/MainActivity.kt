package com.unagit.douuajobsevents.views

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.unagit.douuajobsevents.MyApp
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.RefreshManager
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.helpers.Tab
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.ListPresenter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit
import android.util.Pair as AndroidPair

class MainActivity : BaseActivity(), ListContract.ListView, ItemAdapter.OnClickListener {

    private lateinit var presenter: ListContract.ListPresenter
    private lateinit var mAdapter: ItemAdapter
    private var mTab = Tab.EVENTS
    private var searchMenuItem: MenuItem? = null
    private var searchView: SearchView? = null

    // First refresh after 5 sec.
    private val initialRefreshDelay = TimeUnit.SECONDS.toMillis(5)
    // Refresh each 5 min.
    private val refreshInterval = TimeUnit.MINUTES.toMillis(5)
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = ListPresenter(this, MyApp.dataProvider!!)

        initToolbar()

        initBottomNav()

        initRecycleView()

        initRefresh()

        // Initiate regular refreshment in background with Worker
        // (works even when app is closed)
        RefreshManager().scheduleRefresh()
    }

    private fun initRefresh() {
        val refreshTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    presenter.initiateDataRefresh()
                }
            }
        }
        timer.schedule(refreshTask, initialRefreshDelay, refreshInterval)
    }

    override fun onStart() {
        super.onStart()
        requestItems()
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

    }

    private fun initRecycleView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ItemAdapter(this@MainActivity)
        }
        mAdapter = recyclerView.adapter as ItemAdapter
        val swipeHandler = SwipeHandler(
                this,
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun initBottomNav() {
        bottom_nav.setOnNavigationItemSelectedListener { item ->
            collapseSearchView()

            when (item.itemId) {
                R.id.navigation_events -> {
                    mTab = Tab.EVENTS
                    requestItems()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_vacancies -> {
                    mTab = Tab.VACANCIES
                    requestItems()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_favourites -> {
                    mTab = Tab.FAVOURITES
                    requestItems()
                    return@setOnNavigationItemSelectedListener true
                }
            }

            return@setOnNavigationItemSelectedListener false
        }
    }

    private fun collapseSearchView() {
        if (!searchView?.isIconified!!)
            searchMenuItem?.collapseActionView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        searchMenuItem = menu?.findItem(R.id.menu_search)
        searchMenuItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                requestItems()
                return true
            }

        })
        searchView = searchMenuItem?.actionView as SearchView
        searchView?.apply {
            setIconifiedByDefault(true)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        presenter.search(newText, mTab)
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
            })
        }
        return true
    }

    private fun requestItems() {
        when(mTab) {
            Tab.EVENTS -> presenter.getEvents()
            Tab.FAVOURITES -> presenter.getFavourites()
            Tab.VACANCIES -> presenter.getVacancies()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_refresh -> {
                // User's initiated data refreshment
                presenter.initiateDataRefresh()
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
        timer.cancel()
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
    override fun showItems(items: PagedList<Item>) {
        mAdapter.submitList(items)
    }

    override fun onItemClick(parent: View, position: Int) {
        val guid = mAdapter.getItemAt(position)?.guid
        // Prepare transition animation.
        val imgView = parent.findViewById<View>(R.id.itemImg)
//        val titleView = parent.findViewById<View>(R.id.itemTitle)
        val transImgName = getString(R.string.transition_img_name)
//        val transTitleName = getString(R.string.transition_title_name)
//        val transContainerName = getString(R.string.transition_container_name)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this@MainActivity,
                AndroidPair.create(imgView, transImgName)
//                AndroidPair.create(titleView, transTitleName)
//                AndroidPair.create(parent, transContainerName)
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

    override fun onSwiped(position: Int) {
        val item = mAdapter.getItemAt(position)
        presenter.delete(item!!, position)
    }
}
