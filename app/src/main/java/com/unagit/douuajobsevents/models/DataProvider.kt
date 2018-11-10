package com.unagit.douuajobsevents.models

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.core.text.HtmlCompat
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataProvider(var application: Application?) : Callback<ItemDataWrapper> {

    private val douApiService = DouAPIService.create()
    private val logTag = "RetrofitDataProvider"
    private var items: List<Item>? = null


    companion object {
        private var DB_INSTANCE: AppDatabase? = null

        class InsertAsyncTask : AsyncTask<Item, Void, Void>() {
            override fun doInBackground(vararg params: Item?): Void? {
                DB_INSTANCE?.itemDao()?.insert(params[0]!!)
                return null
            }
        }

        class GetItemsAsyncTask : AsyncTask<Void, Void, List<Item>?>() {

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
//        douApiService.getEvents().enqueue(this)
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
        return if (items != null) items!![position].guid else null
    }

    fun getRefreshDataObservable(): Observable<List<Item>> {

//        val wrapper = douApiService.getEventsObservable()
//        val rawItems = wrapper.map {
//            Log.d(logTag, "received Observable from retrofit call with ${it.items.size} elements.")
//            it.items
//        }
//
//        val items = rawItems.map {
//            it.map {
//                getItemFrom(it)
//            }
//        }


        return douApiService.getEventsObservable()
                .map {
                    Log.d(logTag, "received Observable from retrofit call with ${it.items.size} elements.")
                    it.items

                            // Filter out those items, which are already in local DB
//                            .filter { xmlItem ->
//                                !items!!.any { item ->
//                                    item.guid == xmlItem.guid
//                                }
//                            }

                            // Convert XmlItem into Item and save item into local DB
                            .map { xmlItem ->
                                val item = getItemFrom(xmlItem)
                                DB_INSTANCE?.itemDao()?.insert(item)
                                Log.d(logTag, "Created item with imageUrl ${item.imgUrl}.")
                                item
                            }



                }

    }

    private fun getItemFrom(xmlItem: XmlItem): Item {
//        Log.d(logTag, "-----In getItemFrom--------")
        val guid = xmlItem.guid
        val title = xmlItem.title
//        Log.d(logTag, "$guid")
//        Log.d(logTag, "$title")
//        Log.d(logTag, "description is ${xmlItem.description}")

        // Convert HTML codes into HTML tags
//        val htmlStr = HtmlCompat.fromHtml(xmlItem.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
        // Parse HTML code
//        Log.d(logTag, "htmlStr is $htmlStr ")

//        val doc: Document = Jsoup.parseBodyFragment(htmlStr.toString())
        val doc: Document = Jsoup.parseBodyFragment(xmlItem.description)
//        Log.d(logTag, "doc.body is ${doc.body()} ")

        // Get image url from first paragraph
        val imgUrl = doc.body().selectFirst("p").selectFirst("img").attr("src")
//        Log.d(logTag, "$imgUrl")
        // Get HTML paragraphs omitting first two
        val description = doc.select("body > :gt(1)").html()
//        Log.d(logTag, "$description")

        // Get Spanned from String
//        val spannedDesc = HtmlCompat.fromHtml(itemDesc, HtmlCompat.FROM_HTML_MODE_COMPACT)

        val item = Item(guid, title, imgUrl, description)
//        Log.d(logTag, "${item.guid}, ${item.title}")
        return item

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
        if (wrapper != null) {
            val items = wrapper.items
            items.forEach {
                //                Log.d(logTag, it.title)

//                val description = HtmlCompat.fromHtml(it.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
                val item = Item(it.guid, it.title, "https://", it.description)
                Log.d(logTag, "insert task executed: ${it.guid}")
                InsertAsyncTask().execute(item)

            }
        } else {
            Log.d(logTag, "Received data is empty.")
        }
    }
}