package com.unagit.douuajobsevents.views

import android.util.Log
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

    /**
     * During initialization filteredItems equal to items, received by ItemAdapter as a DataSet.
     * filteredItems are used to show filtered data, depending on user's search input.
     */
    private var filteredItems = mutableListOf<Item>().apply { addAll(items) }


    /**
     * A listener of a RecyclerView item's click.
     */
    interface OnClickListener {
        /**
         * @param parent a ViewHolder view, which is clicked
         * @param guid {@link Item}'s guid
         * @see Item
         */
        fun onItemClicked(parent: View, guid: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredItems[position]
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Item, listener: OnClickListener) {

            // Transform html tags into formatted text
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

    /**
     * Inserts new items into ItemAdapter's data set and refreshes RecyclerView.
     * @param newItems a sub-list, which will be included into data set.
     * @param inPosition position, where sub-list will be inserted.
     */
    fun insertData(newItems: List<Item>, inPosition: Int) {
        this.items.addAll(inPosition, newItems)
        this.filteredItems.addAll(inPosition, newItems)
        notifyItemRangeInserted(inPosition, newItems.size)
    }

    fun setNewData(newItems: List<Item>) {
        this.items.clear()
        this.items.addAll(newItems)
        this.filteredItems.clear()
        this.filteredItems.addAll(newItems)
        notifyDataSetChanged()
    }

    /**
     * Implementation of Filterable interface.
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            /**
             * Returns a set of filtered items, which satisfy a user's search input.
             * @param constraint user's search input.
             * @return filter results.
             * @throws NullPointerException when user's search input is null.
             */
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                if (constraint != null) {
                    val filteredItems =
                            this@ItemAdapter.items
                                    .filter {
                                        Log.d("Search", "${it.title} contains $constraint?")
                                        it.title.contains(constraint, true)
                                    }
                                    .toMutableList()

                    val result = FilterResults()
                    result.values = filteredItems
                    return result

                } else throw NullPointerException("Filter text is null.")
            }

            /**
             * Sets field filteredItems to filtered results and refreshes RecyclerView.
             */
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                this@ItemAdapter.filteredItems = results?.values as MutableList<Item>
                notifyDataSetChanged()
            }
        }
    }
}
