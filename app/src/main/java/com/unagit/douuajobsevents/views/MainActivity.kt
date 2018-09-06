package com.unagit.douuajobsevents.views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.unagit.douuajobsevents.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupTabs()
    }

    fun setupTabs() {
        val viewPager: ViewPager = findViewById(R.id.container_list)
        viewPager.adapter = FragmentsAdapter(supportFragmentManager)

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        for(pos in 0..tabLayout.tabCount) {
            tabLayout.getTabAt(pos)?.text = "title $pos"
        }


    }

}
