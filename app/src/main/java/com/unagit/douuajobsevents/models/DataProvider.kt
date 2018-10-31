package com.unagit.douuajobsevents.models

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataProvider : Callback<ItemDataWrapper> {

    private val douApiService = DouAPIService.create()
    private val logTag = "RetrofitDataProvider"
    init {

    }

    fun getItems() {
        val call = douApiService.getEvents()
        call.enqueue(this)
    }
    override fun onFailure(call: Call<ItemDataWrapper>, t: Throwable) {
        Log.d(logTag, "Failed to get data: ${t.message}")
    }

    override fun onResponse(call: Call<ItemDataWrapper>, response: Response<ItemDataWrapper>) {
        Log.d(logTag, "${call.request().url()}")
        Log.d(logTag, "Received response from retrofit + ${response.isSuccessful}")
        Log.d(logTag, "Received response from retrofit + ${response.body()}")

        // Extract data
        val wrapper = response.body()
        if(wrapper != null) {
            val items = wrapper.items
            items.forEach {
                Log.d(logTag, it.title)
            }
        } else {
            Log.d(logTag, "Received data is empty.")
        }
    }
}