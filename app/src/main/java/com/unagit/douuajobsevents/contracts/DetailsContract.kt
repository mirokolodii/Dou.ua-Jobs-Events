package com.unagit.douuajobsevents.contracts

import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.BasePresenter

interface DetailsContract {

    interface DetailsView {
        fun showItem(item: Item)
    }

    interface DetailsPresenter : BasePresenter<DetailsView> {
        fun getItemFromId(id: String): Item
    }
}