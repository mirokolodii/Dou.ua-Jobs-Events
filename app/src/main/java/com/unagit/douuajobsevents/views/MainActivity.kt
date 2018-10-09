package com.unagit.douuajobsevents.views

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.data.Item
import com.unagit.douuajobsevents.presenters.ListPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ListPresenter.ListView, ItemAdapter.Listener {

    private val presenter = ListPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.attach(this)

//        setupTabs()

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
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ItemAdapter(items, this@MainActivity)
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
