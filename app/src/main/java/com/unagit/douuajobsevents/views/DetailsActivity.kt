package com.unagit.douuajobsevents.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.widget.NestedScrollView
import com.squareup.picasso.Picasso
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.R.id.menu_add_to_calendar
import com.unagit.douuajobsevents.R.id.menu_share
import com.unagit.douuajobsevents.contracts.DetailsContract
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.DetailsPresenter
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), DetailsContract.DetailsView {
    // TODO no detach() method is called for presenter
    private val presenter: DetailsContract.DetailsPresenter = DetailsPresenter()
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setupBarsAndMenu()

        // Get Item from guid
        val guid = intent.getStringExtra(getString(R.string.extra_guid_id))

        presenter.attach(this, application)
        presenter.requestItemFromId(guid)

        // FAB onClickListener:
        // Opens item's URL in a web browser
        fab.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(guid)))
        }

        // Hide fab once scrolled to the bottom,
        // un-hide otherwise
        nestedScrollView.setOnScrollChangeListener {
            v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v != null && v.canScrollVertically(1 /* positive direction means scrolling down*/)) {
                fab.show()
            } else {
                fab.hide()
            }
        }
    }

    /**
     * Setup top and bottom bars.
     * Top bar includes navigation,
     * while bottom one includes menu elements.
     */
    private fun setupBarsAndMenu() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) //remove title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Need this code for reversed animation transition to work on back arrow pressed
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // Set bottom bar menu
        bottom_bar.inflateMenu(R.menu.details_bottom_menu)
        bottom_bar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when(it.itemId) {
                menu_share -> {
                    share()
                    true
                }
                menu_add_to_calendar -> {
                    addToCalendar()
                    true
                } else -> super.onOptionsItemSelected(it)
            }
        }
    }

    /**
     * Showes Item, received from presenter, on the screen.
     */
    override fun showItem(item: Item) {
        this.item = item
        detailedItemTitle.text = HtmlCompat.fromHtml(item.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
        detailedItemDetails.text = HtmlCompat.fromHtml(item.description, HtmlCompat.FROM_HTML_MODE_COMPACT)

        //Load img into ImgView
        Picasso
                .get()
                .load(item.imgUrl)
//                .resize(200, 150)
//                .centerInside()
                .into(detailedItemImg)

        // Set links in description to be clickable
        detailedItemDetails.movementMethod = LinkMovementMethod.getInstance()
    }

    /**
     * Setup and starts activity for add-to-calendar intent.
     */
    private fun addToCalendar() {
        if(this.item == null) {
            Log.e(this.javaClass.simpleName, "Item is null.")
            return
        }
        val title = HtmlCompat.fromHtml(this.item!!.title, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
        val guid = this.item!!.guid
        val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, "$title $guid")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
        startActivity(intent)
    }

    /**
     * Setup and starts activity for 'share' intent.
     */
    private fun share() {
        if(this.item == null) {
            Log.e(this.javaClass.simpleName, "Item is null.")
            return
        }
        val title = HtmlCompat.fromHtml(this.item!!.title, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
        val guid = this.item!!.guid
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "$title $guid")
        startActivity(Intent.createChooser(intent, "Share with"))
    }
}

