package com.unagit.douuajobsevents.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NavUtils
import androidx.core.text.HtmlCompat
import androidx.core.widget.NestedScrollView
import com.squareup.picasso.Picasso
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.R.id.*
import com.unagit.douuajobsevents.contracts.DetailsContract
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.DetailsPresenter
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), DetailsContract.DetailsView {
    private val presenter: DetailsContract.DetailsPresenter = DetailsPresenter()
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)
//        setSupportActionBar(bottom_bar)
        supportActionBar?.setDisplayShowTitleEnabled(false) //remove title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Need this code for reversed animation transition to work on back arrow pressed.
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        bottom_bar.inflateMenu(R.menu.details_bottom_menu)
        bottom_bar.setOnMenuItemClickListener {

            return@setOnMenuItemClickListener when(it.itemId) {
//            R.id.home -> {
//                //
//                NavUtils.navigateUpFromSameTask(this)
//                true
//            }
                menu_share -> {
//                val snackbar = Snackbar.make(activityDetailsLayout,
//                        "Share clicked",
//                        Snackbar.LENGTH_SHORT)
//                val layoutParams = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
//                layoutParams.setMargins(0, 0, 0, bottom_bar.height);
//                snackbar.view.layoutParams = layoutParams
//                snackbar.show()
                    Share()
                    true
                }
                menu_add_to_calendar -> {
                    addToCalendar()

                    true
                } else -> super.onOptionsItemSelected(it)
            }
        }




        // Get Item from guid
        val guid = intent.getStringExtra(getString(R.string.extra_guid_id))

        presenter.attach(this, application)
        presenter.requestItemFromId(guid)

        // Open event in browser
        fab.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(guid)))
        }

        // Hide fab once scrolled to the bottom
        nestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v != null && v.canScrollVertically(1)) {
                fab.show()
//            Snackbar.make(v, "Not at the bottom", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()

            } else {
//                Snackbar.make(v!!, "At the bottom", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show()
                fab.hide()
            }
        }
    }

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



//        detailedItemTitle.setOnClickListener { _ ->
//            Snackbar.make(activityDetailsLayout, item.title, Snackbar.LENGTH_SHORT).show()
//        }
//
//        detailedItemDetails.setOnClickListener { _ ->
//            Snackbar.make(activityDetailsLayout, "Details clicked ${detailedItemDetails.linksClickable}", Snackbar.LENGTH_SHORT).show()
//        }

        // Set links in description to be clickable
        detailedItemDetails.movementMethod = LinkMovementMethod.getInstance() //.setMovementMethod(LinkMovementMethod.getInstance())
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.details_bottom_menu, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when(item?.itemId) {
//            R.id.home -> {
//                //
//                NavUtils.navigateUpFromSameTask(this)
//                true
//            }
            menu_share -> {
//                val snackbar = Snackbar.make(activityDetailsLayout,
//                        "Share clicked",
//                        Snackbar.LENGTH_SHORT)
//                val layoutParams = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
//                layoutParams.setMargins(0, 0, 0, bottom_bar.height);
//                snackbar.view.layoutParams = layoutParams
//                snackbar.show()
                Share()
                true
            }
            menu_add_to_calendar -> {
                addToCalendar()

                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

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

    private fun Share() {
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

