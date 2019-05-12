package com.unagit.douuajobsevents

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
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
        appComponent = initDagger(this)

        // Enable Firebase Analytics if not debug build
        if (!BuildConfig.DEBUG) {
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)
        }

    }

    private fun initDagger(app: MyApp): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()
}