package com.unagit.douuajobsevents.dagger

import com.unagit.douuajobsevents.data.DataProvider
import com.unagit.douuajobsevents.ui.details.DetailsContract
import com.unagit.douuajobsevents.ui.details.DetailsPresenter
import com.unagit.douuajobsevents.ui.list.ListContract
import com.unagit.douuajobsevents.ui.list.ListPresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    fun provideListPresenter(dataProvider: DataProvider): ListContract.ListPresenter = ListPresenter(dataProvider)

    @Provides
    fun provideDetailsPresenter(dataProvider: DataProvider): DetailsContract.DetailsPresenter = DetailsPresenter(dataProvider)
}