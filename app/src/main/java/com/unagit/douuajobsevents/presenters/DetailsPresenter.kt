package com.unagit.douuajobsevents.presenters

import android.app.Application
import com.unagit.douuajobsevents.contracts.DetailsContract
import com.unagit.douuajobsevents.models.Item

class DetailsPresenter: DetailsContract.DetailsPresenter {
    private var view: DetailsContract.DetailsView? = null
    override fun attach(view: DetailsContract.DetailsView, application: Application) {
        this.view = view
    }

    override fun detach() {
        this.view = null
    }

    override fun requestItemFromId(id: String) {
        val item = Item(id, "test title", "image url", "description")
        view?.showItem(item)
    }
}