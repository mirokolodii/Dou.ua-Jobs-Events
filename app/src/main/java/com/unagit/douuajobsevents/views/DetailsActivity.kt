package com.unagit.douuajobsevents.views

import android.os.Bundle
import android.text.Spanned
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.models.DataInjector
import kotlinx.android.synthetic.main.activity_details.*
import org.jsoup.Jsoup

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) //remove title

        // Get Item from guid
        val guid = intent.getStringExtra(getString(R.string.extra_guid_id))
        val item = DataInjector.getItemById(guid)

        //Load img into ImgView
        Picasso
                .get()
                .load(item.imgUrl)
//                .resize(200, 150)
//                .centerInside()
                .into(detailedItemImg)

        detailedItemTitle.text = item.title
        val description = decodeHtml(item.description)
        val description2 = cleanupHtml(description.toString())
        detailedItemDetails.text = description2

        detailedItemTitle.setOnClickListener { _ ->
            Snackbar.make(activityDetailsLayout, item.title, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun decodeHtml(str: String): Spanned {
        var result = HtmlCompat.fromHtml(str, HtmlCompat.FROM_HTML_MODE_COMPACT)

        while(result.toString() != str) {
            result = HtmlCompat.fromHtml(str, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }

        return result
    }

    private fun cleanupHtml(str: String) : String {
        Jsoup.parse(str)
        return ""
    }
}

