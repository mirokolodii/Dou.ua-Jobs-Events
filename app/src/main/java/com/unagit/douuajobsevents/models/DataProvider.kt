package com.unagit.douuajobsevents.models

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataProvider : Callback<ResponseBody> {

    private val douApiService = DouAPIService.create()
    private val logTag = "RetrofitDataProvider"
    init {

    }

    fun getItems() {
        val call = douApiService.getRawEvents()
        call.enqueue(this)
    }
    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        Log.d(logTag, "Failed to parse xml")
    }

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
        Log.d(logTag, "${call.request().url()}")
        Log.d(logTag, "Received response from retrofit + ${response.isSuccessful}")
        Log.d(logTag, "Received response from retrofit + ${response.body()}")
    }
}