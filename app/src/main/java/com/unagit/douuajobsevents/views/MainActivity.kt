package com.unagit.douuajobsevents.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.unagit.douuajobsevents.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupTabs()
    }

    private fun setupTabs() {
//        val viewPager: ViewPager = findViewById(R.id.container_list)
//        viewPager.adapter = FragmentsAdapter(supportFragmentManager)
        container_list.adapter = FragmentsAdapter(supportFragmentManager)

//        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
//        tabLayout.setupWithViewPager(viewPager)
        tab_layout.setupWithViewPager(container_list)
        // Set tab titles
        for(pos in 0 until tab_layout.tabCount) {
            tab_layout.getTabAt(pos)?.text = "title $pos"
        }
    }
}
