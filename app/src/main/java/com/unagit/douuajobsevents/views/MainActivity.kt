package com.unagit.douuajobsevents.views

import android.app.ActivityOptions
import android.content.Intent
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
import kotlinx.android.synthetic.main.list_item.*
import android.util.Pair as AndroidPair

class MainActivity : AppCompatActivity(), ListContract.ListView {

    private val presenter: ListContract.ListPresenter = ListPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attach(this)
        presenter.getItems()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }


    override fun showItems(items: List<Item>) {
        progressBar.visibility = View.GONE
        showItemsInList(items)
    }

    private fun showItemsInList(items: List<Item>) {
        // Listener for an item click in a list of items
        val listener = object: ItemAdapter.Listener {
            override fun onItemClicked(position: Int) {
//                Snackbar.make(activityMainLayout, item.guid, Snackbar.LENGTH_SHORT)
//                        .show()

                presenter.itemClicked(position)

            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ItemAdapter(items, listener)
        }
    }

    override fun showDetails(position: Int, item: Item) {
        val itemView = recyclerView.layoutManager?.getChildAt(position)
        val imgView = itemView?.findViewById<View>(R.id.itemImg)
        val titleView = itemView?.findViewById<View>(R.id.itemTitle)

        val detailsIntent = Intent(this@MainActivity, DetailsActivity::class.java)
        detailsIntent.putExtra(getString(R.string.extra_guid_id), item.guid)
        val transImgName = getString(R.string.transition_img_name)
        val transTitleName = getString(R.string.transition_title_name)
        val transContainerName = getString(R.string.transition_container_name)
        val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this@MainActivity,

                AndroidPair.create(imgView, transImgName),
                AndroidPair.create(titleView, transTitleName),
                AndroidPair.create(itemView, transContainerName)
        )



        startActivity(detailsIntent, transitionActivityOptions.toBundle())

    }

    // To simplify development we use single activity w/o fragments.
    // Will add fragments later on, if needed.
//    private fun setupTabs() {
////        val viewPager: ViewPager = findViewById(R.id.container_list)
////        viewPager.adapter = FragmentsAdapter(supportFragmentManager)
//        container_list.adapter = FragmentsAdapter(supportFragmentManager)
//
////        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
////        tabLayout.setupWithViewPager(viewPager)
//        tab_layout.setupWithViewPager(container_list)
//        // Set tab titles
//        for(pos in 0 until tab_layout.tabCount) {
//            tab_layout.getTabAt(pos)?.text = "title $pos"
//        }
//    }
}
