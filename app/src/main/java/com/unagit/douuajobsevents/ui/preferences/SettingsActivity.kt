package com.unagit.douuajobsevents.ui.preferences

import android.os.Bundle
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_details.*

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
