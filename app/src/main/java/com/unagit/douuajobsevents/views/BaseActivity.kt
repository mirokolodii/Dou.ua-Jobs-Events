package com.unagit.douuajobsevents.views

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {
    /**
     * Shows message on the screen.
     * @param text specifying a message
     */
    open fun showMessage(text: String) {
        Snackbar.make(
                findViewById(android.R.id.content),
                text,
                Snackbar.LENGTH_SHORT)
                .show()
    }
}