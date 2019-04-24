package com.unagit.douuajobsevents.data

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.GsonBuilder
import com.unagit.douuajobsevents.BuildConfig
import com.unagit.douuajobsevents.R
import com.unagit.douuajobsevents.model.Image
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AppRemoteConfig {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    var images: List<Image>

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

        images = refreshImages()

        fetchAndActivateInBackground()
    }

    private fun fetchAndActivateInBackground() {
        val completable = Completable.create { emitter ->
            remoteConfig.fetchAndActivate()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val updated = task.result
                            if (updated!!) {
                                images = refreshImages()
                            }
                            emitter.onComplete()
                        } else {
                            emitter.onError(Error("Remote Config fetch has failed"))
                        }
                    }
        }

        completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        Log.d("remote_config", "fetch completed")
                    }

                    override fun onSubscribe(d: Disposable) {}

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
    }

    private fun refreshImages(): List<Image> {
        val jsonStr = remoteConfig.getString(REMOTE_CONF_IMAGES_KEY)
        val gson = GsonBuilder().create()
//        val imageListType = object : TypeToken<List<Image>>() {}.type
//        images = gson.fromJson(obj, imageListType)
        val updatedImages = gson.fromJson(jsonStr, Array<Image>::class.java).toList()
        println(updatedImages)
        return updatedImages
    }
}