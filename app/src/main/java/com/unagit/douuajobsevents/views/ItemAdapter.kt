package com.unagit.douuajobsevents.views

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.text.HtmlCompat
import com.squareup.picasso.Picasso
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.models.Item
import kotlinx.android.synthetic.main.list_item.view.*

class ItemAdapter(private var items: MutableList<Item>, private val listener: OnClickListener)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>(),
        Filterable {

    var filteredItems = items

    interface OnClickListener {
        fun onItemClicked(parent: View, guid: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = items[position]
        val item = filteredItems[position]
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int {
//        return items.size
        return filteredItems.size
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
        this.filteredItems.addAll(inPosition, newItems)
        notifyItemRangeInserted(inPosition, newItems.size)
//        Log.d("ItemAdapter", "Items after insert: ${this.items?.size}")

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                if (constraint != null) {
                    val filteredItems = this@ItemAdapter.items.filter {
                        it.title.contains(constraint, true)
                    }

                }

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.values
            }
        }
    }
}
