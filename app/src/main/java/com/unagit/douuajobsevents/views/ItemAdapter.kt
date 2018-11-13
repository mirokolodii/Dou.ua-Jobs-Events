package com.unagit.douuajobsevents.views

import android.text.Html
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.squareup.picasso.Picasso
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.models.Item
import kotlinx.android.synthetic.main.list_item.view.*

class ItemAdapter(private var items: MutableList<Item>, private val listener: OnClickListener)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Item, listener: OnClickListener) {
            itemView.itemTitle.text = HtmlCompat.fromHtml(item.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
            Picasso
                    .get()
                    .load(item.imgUrl)
//                    .resize(200, 150)
//                    .centerInside()
                    .into(itemView.itemImg)

            itemView.setOnClickListener { listener.onItemClicked(itemView, item.guid) }

        }
    }

    fun insertData(newItems: List<Item>, inPosition: Int) {
//        Log.d("ItemAdapter", "Items: ${this.items?.size}")
        this.items.addAll(inPosition, newItems)
        notifyItemRangeInserted(inPosition, newItems.size)
//        Log.d("ItemAdapter", "Items after insert: ${this.items?.size}")

    }

    interface OnClickListener {
        fun onItemClicked(parent: View, guid: String)
    }
}