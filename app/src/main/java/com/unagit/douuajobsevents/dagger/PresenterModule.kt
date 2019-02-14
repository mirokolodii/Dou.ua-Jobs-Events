package com.unagit.douuajobsevents.dagger

import com.unagit.douuajobsevents.contracts.DetailsContract
import com.unagit.douuajobsevents.contracts.ListContract
import com.unagit.douuajobsevents.models.DataProvider
import com.unagit.douuajobsevents.presenters.DetailsPresenter
import com.unagit.douuajobsevents.presenters.ListPresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    fun provideListPresenter(dataProvider: DataProvider): ListContract.ListPresenter = ListPresenter(dataProvider)

    @Provides
    fun provideDetailsPresenter(dataProvider: DataProvider): DetailsContract.DetailsPresenter = DetailsPresenter(dataProvider)
}