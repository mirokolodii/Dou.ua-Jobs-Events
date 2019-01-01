package com.unagit.douuajobsevents.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.ViewAnimationUtils
import android.view.View
import android.transition.Transition
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.core.text.HtmlCompat
import androidx.core.widget.NestedScrollView
import com.squareup.picasso.Picasso
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.R.id.*
import com.unagit.douuajobsevents.contracts.DetailsContract
import com.unagit.douuajobsevents.models.Item
import com.unagit.douuajobsevents.presenters.DetailsPresenter
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : BaseActivity(), DetailsContract.DetailsView {
    private val presenter: DetailsContract.DetailsPresenter = DetailsPresenter()
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setupBarsAndMenu()

        // Get Item from guid
        val guid = intent.getStringExtra(getString(R.string.extra_guid_id))

        presenter.attach(this)
        presenter.requestItemFromId(guid)

        initFAB(guid)

        addEnterAnim()


    }

    private fun initFAB(guid: String) {
        // FAB onClickListener:
        // Opens item's URL in a web browser
        fab.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(guid)))
        }

        // Hide fab once scrolled to the bottom,
        // un-hide otherwise
        nestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v != null && v.canScrollVertically(1 /* positive direction means scrolling down*/)) {
                fab.show()
            } else {
                fab.hide()
            }
        }
    }

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    override fun onBackPressed() {
        addExitAnim()
        super.onBackPressed()
    }

    private fun addEnterAnim() {
        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                enterReveal()
                window.sharedElementEnterTransition.removeListener(this)
            }
            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionStart(transition: Transition?) {
            }
        })

    }

    private fun addExitAnim() {
        window.returnTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition?) {
                exitReveal()
                window.returnTransition.removeListener(this)

            }
            override fun onTransitionEnd(transition: Transition?) {
            }

            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }
        })
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
            return@setOnMenuItemClickListener when (it.itemId) {
                menu_share -> {
                    share()
                    true
                }
                menu_add_to_calendar -> {
                    addToCalendar()
                    true
                }
                menu_favourites -> {
                    presenter.changeItemFavVal(this.item!!)
                    true
                }
                else -> super.onOptionsItemSelected(it)
            }
        }
    }

    /**
     * Shows Item, received from presenter, on the screen.
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

        // Set favourite menu icon, depending on whether or not item isFavourite
        showAsFavourite(item.isFavourite)
    }

    /**
     * Updates Item's isFavourite value with new one.
     * Changes favourites icon.
     * @param isFavourite new value of isFavourite.
     * True - added to fav, false - removed from fav
     */
    override fun showAsFavourite(isFavourite: Boolean) {
        this.item?.isFavourite = isFavourite
        val favMenuItem = bottom_bar.menu.findItem(R.id.menu_favourites)
        if (isFavourite) {
            favMenuItem.setIcon(R.drawable.ic_favorite)
        } else {
            favMenuItem.setIcon(R.drawable.ic_favorite_border)
        }
    }

    /**
     * Setup and starts activity for add-to-calendar intent.
     */
    private fun addToCalendar() {
        if (this.item == null) {
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
        if (this.item == null) {
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

    fun enterReveal() {
        // get the center for the clipping circle
        val cx = appbar.measuredWidth / 2
        val cy = appbar.measuredHeight / 2

        // get the final radius for the clipping circle
        val finalRadius = Math.max(appbar.width, appbar.height) / 2

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(appbar, cx, cy, 0f, finalRadius.toFloat())

        // make the view visible and start the animation
        appbar.visibility = View.VISIBLE
        anim.start()
    }

    fun exitReveal() {
        // get the center for the clipping circle
        val cx = appbar.measuredWidth / 2
        val cy = appbar.measuredHeight / 2

        // get the final radius for the clipping circle
        val initialRadius = Math.max(appbar.width, appbar.height) / 2

        // create the animation (the final radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(appbar, cx, cy, initialRadius.toFloat(), 0f)

        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                appbar.visibility = View.INVISIBLE
            }
        })

        // start the animation
        anim.start()
    }
}

