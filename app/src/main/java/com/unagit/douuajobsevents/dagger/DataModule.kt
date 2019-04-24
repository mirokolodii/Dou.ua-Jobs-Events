package com.unagit.douuajobsevents.dagger

import android.content.Context
import com.unagit.douuajobsevents.data.AppDatabase
import com.unagit.douuajobsevents.data.AppRemoteConfig
import com.unagit.douuajobsevents.data.DataProvider
import com.unagit.douuajobsevents.data.DouAPIService
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
    fun provideRemoteConfigInstance(): AppRemoteConfig = AppRemoteConfig()

    @Provides
    @Singleton
    fun provideDouApiService(): DouAPIService = DouAPIService.create()

    @Provides
    @Singleton
    fun provideDataProvider(dbInstance: AppDatabase,
                            douAPIService: DouAPIService,
                            appRemoteConfig: AppRemoteConfig): DataProvider = DataProvider(dbInstance, douAPIService, appRemoteConfig)

}