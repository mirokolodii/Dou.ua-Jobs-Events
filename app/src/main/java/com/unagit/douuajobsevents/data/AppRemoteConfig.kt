package com.unagit.douuajobsevents.data

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.GsonBuilder
import com.unagit.douuajobsevents.BuildConfig
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.model.Image

class AppRemoteConfig {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    companion object {
        private const val REMOTE_CONF_IMAGES_KEY = "images"

    }

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .setMinimumFetchIntervalInSeconds(4200)
                .build()
        remoteConfig.setConfigSettings(configSettings)
        remoteConfig.setDefaults(R.xml.remote_config_defaults)

        //TODO: run below in background thread
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val updated = task.result
                        Log.d("remote_config", "Config params updated: $updated")
                    } else {
                        Log.d("remote_config", "fetched failed")
                    }
                }
    }

    fun getImages(): List<Image> {
        val jsonStr = remoteConfig.getString(REMOTE_CONF_IMAGES_KEY)
        val gson = GsonBuilder().create()
//        val imageListType = object : TypeToken<List<Image>>() {}.type
//        images = gson.fromJson(obj, imageListType)
        return gson.fromJson(jsonStr, Array<Image>::class.java).toList()
    }
}