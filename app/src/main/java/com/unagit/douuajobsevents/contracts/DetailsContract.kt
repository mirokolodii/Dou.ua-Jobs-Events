package com.unagit.douuajobsevents.contracts

import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.BasePresenter

/**
 * Contract between DetailsView and DetailsPresenter.
 * @version %I%
 * @author Myroslav Kolodii
 */
interface DetailsContract {

    // View
    interface DetailsView {
        /**
         * Shows item to user.
         * @param item to be shown.
         * @see Item
         */
        fun showItem(item: Item)
    }

    // Presenter
    interface DetailsPresenter : BasePresenter<DetailsView> {
        /**
         * Requests Item from repository by its id.
         * @param id of an item.
         * @see Item
         */
        fun requestItemFromId(id: String)
    }
}