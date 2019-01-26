package com.unagit.douuajobsevents.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.models.Item
import kotlinx.android.synthetic.main.list_item.view.*

class ItemAdapter(private val listener: OnClickListener)
    : PagedListAdapter<Item, ItemAdapter.ViewHolder>(ITEM_COMPARATOR) {

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                    oldItem.guid == newItem.guid

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                    oldItem == newItem
        }
    }

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
        val item = getItem(position)
        holder.bind(item, listener)

        // Set bottom margin on last item
        val margin = holder.itemView.resources.getDimension(R.dimen.list_item_margin).toInt()
        val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (position == itemCount - 1) {
            params.setMargins(margin, margin, margin, margin)
        } else {
            params.setMargins(margin, margin, margin, 0)
        }
        holder.itemView.requestLayout()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Item?, listener: OnClickListener) {

            // Transform html tags into formatted text
            // TODO I'd prefer to parse text on lower level (e.g. when item object is created, or even on object receiving).
            // TODO HtmlCompat.fromHtml() method is too heavy to use it on EACH row binding.
            if (item == null) {
                return
            }
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

    fun getItemAt(position: Int): Item? {
        return getItem(position)
    }
}
