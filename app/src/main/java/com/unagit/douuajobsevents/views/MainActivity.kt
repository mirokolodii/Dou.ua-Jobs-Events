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

class MainActivity : AppCompatActivity(), ListContract.ListView, ItemAdapter.OnClickListener{

    private val presenter: ListContract.ListPresenter = ListPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attach(this, application)
        presenter.getItems()

        button.setOnClickListener {
            presenter.refreshData()
        }



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
