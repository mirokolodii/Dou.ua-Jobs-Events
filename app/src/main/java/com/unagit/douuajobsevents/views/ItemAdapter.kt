package com.unagit.douuajobsevents.views

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.models.Item
import kotlinx.android.synthetic.main.list_item.view.*

class ItemAdapter(private var items: List<Item>, private val listener: Listener)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, listener, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Item, listener: Listener, position: Int) {
            itemView.itemTitle.text = item.title
            Picasso
                    .get()
                    .load(item.imgUrl)
                    .resize(200, 150)
                    .centerInside()
                    .into(itemView.itemImg)

            itemView.setOnClickListener {listener.onItemClicked(position)}

        }
    }
    interface Listener {
        fun onItemClicked(position: Int)
    }
}