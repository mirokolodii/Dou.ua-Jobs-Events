package com.unagit.douuajobsevents.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionManager
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.R.id.menu_add_to_calendar
import com.unagit.douuajobsevents.R.id.menu_share
import com.unagit.douuajobsevents.contracts.DetailsContract
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.DetailsPresenter
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), DetailsContract.DetailsView {
    val presenter: DetailsContract.DetailsPresenter = DetailsPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(bottom_bar)
        supportActionBar?.setDisplayShowTitleEnabled(false) //remove title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.details_bottom_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when(item?.itemId) {
            menu_share -> {
                Snackbar.make(activityDetailsLayout,
                        "Share clicked",
                        Snackbar.LENGTH_SHORT)
                        .show()
                true
            }
            menu_add_to_calendar -> {
                Snackbar.make(activityDetailsLayout,
                        "Add to calendar clicked",
                        Snackbar.LENGTH_SHORT)
                        .show()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }
}

