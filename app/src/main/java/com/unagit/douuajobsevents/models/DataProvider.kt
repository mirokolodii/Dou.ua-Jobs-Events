package com.unagit.douuajobsevents.models

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataProvider(var application: Application?) : Callback<ItemDataWrapper> {

    private val douApiService = DouAPIService.create()
    private val logTag = "RetrofitDataProvider"
    private var items: List<Item>? = null



    companion object {
        private var DB_INSTANCE: AppDatabase? = null

        class InsertAsyncTask: AsyncTask<Item, Void, Void>() {
            override fun doInBackground(vararg params: Item?): Void? {
                DB_INSTANCE?.itemDao()?.insert(params[0]!!)
                return null
            }
        }

        class GetItemsAsyncTask: AsyncTask<Void, Void, List<Item>?>() {

            override fun doInBackground(vararg params: Void?): List<Item>? {
                return DB_INSTANCE?.itemDao()?.getItems()
            }

            override fun onPostExecute(result: List<Item>?) {
//                super.onPostExecute(result)
                result?.forEach {
                    Log.d("DBTest", it.title)
                }

            }
        }
    }

    init {
        DB_INSTANCE = AppDatabase.getInstance(application!!)

        // Refresh data from network
        douApiService.getEvents().enqueue(this)
    }

    fun detach() {
        this.application = null
    }


    fun getItemsObservable(): Observable<List<Item>> {
        return Observable
                .create<List<Item>> { emitter ->
            items = DB_INSTANCE!!.itemDao().getItems()
            emitter.onNext(items!!)
            emitter.onComplete()
        }

    }

    fun getGuidForItemIn(position: Int): String? {
        return if(items != null) items!![position].guid else null
    }

    fun getRefreshDataObservable(): Observable<List<Item>> {

    }

    // Retrofit callback
    override fun onFailure(call: Call<ItemDataWrapper>, t: Throwable) {
        Log.d(logTag, "Failed to get data: ${t.message}")
    }

    // Retrofit callback
    override fun onResponse(call: Call<ItemDataWrapper>, response: Response<ItemDataWrapper>) {
//        Log.d(logTag, "${call.request().url()}")
//        Log.d(logTag, "Received response from retrofit + ${response.isSuccessful}")
//        Log.d(logTag, "Received response from retrofit + ${response.body()}")

        // Extract data
        val wrapper = response.body()
        if(wrapper != null) {
            val items = wrapper.items
            items.forEach {
//                Log.d(logTag, it.title)

//                val description = HtmlCompat.fromHtml(it.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
                val item = Item(it.link, it.title, "https://", it.description)
                Log.d(logTag, "insert task executed: ${it.link}")
                InsertAsyncTask().execute(item)

            }
        } else {
            Log.d(logTag, "Received data is empty.")
        }
    }
}