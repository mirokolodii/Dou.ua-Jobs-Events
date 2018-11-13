package com.unagit.douuajobsevents.views

import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.contracts.DetailsContract
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.DetailsPresenter
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), DetailsContract.DetailsView {
    val presenter: DetailsContract.DetailsPresenter = DetailsPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) //remove title

        // Get Item from guid
        val guid = intent.getStringExtra(getString(R.string.extra_guid_id))
        Snackbar.make(activityDetailsLayout, guid, Snackbar.LENGTH_SHORT).show()

        presenter.attach(this, application)
        presenter.requestItemFromId(guid)
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

        // Set links in details to be clickable
        detailedItemDetails.movementMethod = LinkMovementMethod.getInstance() //.setMovementMethod(LinkMovementMethod.getInstance())
    }
}

