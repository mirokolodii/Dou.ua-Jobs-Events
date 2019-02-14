package com.unagit.douuajobsevents.dagger

import android.content.Context
import com.unagit.douuajobsevents.models.AppDatabase
import com.unagit.douuajobsevents.models.DataProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideDBInstance(context: Context): AppDatabase = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideDataProvider(dbInstance: AppDatabase): DataProvider = DataProvider(dbInstance)
}