package com.unagit.douuajobsevents.models

import com.unagit.douuajobsevents.helpers.RetrofitConstants
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET

/**
 * Keeps Retrofit API calls
 * and static method to create a new Retrofit instance.
 */
interface DouAPIService {

    /**
     * Retrofit GET call to receive events.
     */
    @GET(RetrofitConstants.DOU_UA_CALENDAR_API_URL)
    fun getEventsObservable(): Observable<ItemDataWrapper>

    companion object {
        /**
         * Creates a new Retrofit call instance.
         * A deprecated Xml converter 'SimpleXml' is used,
         * because I was not able to find alternative one with required functionality.
         */
        fun create(): DouAPIService {
            val retrofit = Retrofit.Builder()
                    .baseUrl(RetrofitConstants.DOU_UA_BASE_API_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(DouAPIService::class.java)
        }
    }


}