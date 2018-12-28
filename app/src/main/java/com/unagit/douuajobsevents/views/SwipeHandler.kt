package com.unagit.douuajobsevents.views

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.unagit.douuajobsevents.contracts.ListContract

class SwipeHandler(
        val view: ListContract.ListView,
        dragDirs: Int,
        swipeDirs: Int) :
        ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        view.showMessage("Swiped position ${viewHolder.adapterPosition} at direction $direction")
        view.onSwiped(viewHolder.adapterPosition)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

}