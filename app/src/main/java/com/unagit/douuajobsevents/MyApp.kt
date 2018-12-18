package com.unagit.douuajobsevents

import android.app.Application
import com.unagit.douuajobsevents.models.DataProvider

class MyApp : Application() {


    companion object {
        var dataProviderInstance : DataProvider? = null
    }

    override fun onCreate() {
        super.onCreate()
        dataProviderInstance = DataProvider(this)
    }


}