package com.unagit.douuajobsevents.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.ListPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ListPresenter.ListView {

    private val presenter = ListPresenter()

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
        setupItemList(items)
    }

    private fun setupItemList(items: List<Item>) {
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = ItemAdapter(items)


        // Listener for an item click in a list of items
        val listener = object: ItemAdapter.Listener {
            override fun onItemClicked(item: Item) {
                Snackbar.make(activityMainLayout, item.title, Snackbar.LENGTH_SHORT)
                        .show()
            }

        }



        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ItemAdapter(items, listener)
        }
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
