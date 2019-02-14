package com.unagit.douuajobsevents.ui.list

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.unagit.douuajobsevents.R

class SwipeHandler(
        val view: ListContract.ListView,
        dragDirs: Int,
        swipeDirs: Int) :
        ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    private val background = ColorDrawable(Color.RED)


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        view.onSwiped(viewHolder.adapterPosition)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val icon = itemView.context.getDrawable(R.drawable.ic_delete_white_24dp)

        val iconTop = itemView.top + (itemView.height - icon!!.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight
        var iconLeft = 0
        var iconRight = 0
        val iconMargin = icon.intrinsicWidth

        if (dX > 0) { // Swiping to the right
            background.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.left + dX.toInt(),
                    itemView.bottom
            )

            iconLeft = itemView.left + iconMargin
            iconRight = itemView.left + icon.intrinsicWidth + iconMargin

        } else if (dX < 0) { // Swiping to the left
            background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
            )

            iconLeft = itemView.right - icon.intrinsicWidth - iconMargin
            iconRight = itemView.right - iconMargin

        } /*else { // No swiping
            background.setBounds(0, 0, 0, 0)
            iconLeft = 0
            iconRight = 0
        }*/

        icon.setBounds(
                iconLeft,
                iconTop,
                iconRight,
                iconBottom
        )

        background.draw(c)
        icon.draw(c)
    }
}