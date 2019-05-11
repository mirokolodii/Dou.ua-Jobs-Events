package com.unagit.douuajobsevents.ui.preferences

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.unagit.douuajobsevents.R


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        // Rate app
        findPreference<Preference>(getString(R.string.rate_key))?.onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    launchMarket()
                    true
                }

        // Privacy policy
        findPreference<Preference>(getString(R.string.privacy_key))?.onPreferenceClickListener =
                Preference.OnPreferenceClickListener {
                    showPrivacyPolicy()
                    true
                }
    }

    private fun showPrivacyPolicy() {
        fragmentManager!!
                .beginTransaction()
                .replace(R.id.settings, PrivacyFragment())
                .addToBackStack("PrivacyFragment")
                .commit()
    }


    private fun launchMarket() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context!!.packageName)))

        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context!!.packageName)))

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                    context,
                    getString(R.string.rate_not_in_market_error),
                    Toast.LENGTH_LONG).show()

        }

    }
}