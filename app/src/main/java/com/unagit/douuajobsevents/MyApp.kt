package com.unagit.douuajobsevents

import android.app.Application
import com.unagit.douuajobsevents.models.AppDatabase
import com.unagit.douuajobsevents.models.DataProvider

class MyApp : Application() {


    companion object {
        var dataProvider : DataProvider? = null
    }

    override fun onCreate() {
        super.onCreate()
        val appDBInstance = AppDatabase.getInstance(this)
        dataProvider = DataProvider(appDBInstance!!)
    }


}