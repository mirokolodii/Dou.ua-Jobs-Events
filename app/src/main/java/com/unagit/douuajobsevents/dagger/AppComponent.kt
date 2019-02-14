package com.unagit.douuajobsevents.dagger

import com.unagit.douuajobsevents.ui.details.DetailsActivity
import com.unagit.douuajobsevents.ui.list.MainActivity
import com.unagit.douuajobsevents.workers.RefreshWorker
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    PresenterModule::class,
    DataModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: DetailsActivity)
    fun inject(target: RefreshWorker)
}