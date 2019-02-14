package com.unagit.douuajobsevents

import android.app.Application
import com.unagit.douuajobsevents.dagger.AppComponent
import com.unagit.douuajobsevents.dagger.AppModule
import com.unagit.douuajobsevents.dagger.DaggerAppComponent

/**
 * Keeps app's singletons.
 */
class MyApp : Application() {

    lateinit var appComponent: AppComponent

//    companion object {
//        var dataProvider: DataProvider? = null
//    }

    override fun onCreate() {
        super.onCreate()
//        val appDBInstance = AppDatabase.getInstance(this)
//        dataProvider = DataProvider(appDBInstance)
        appComponent = initDagger(this)
    }

    private fun initDagger(app: MyApp): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()
}