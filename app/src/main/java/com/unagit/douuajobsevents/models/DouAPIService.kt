package com.unagit.douuajobsevents.models

import com.unagit.douuajobsevents.helpers.RetrofitConstants
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET

interface DouAPIService {
    @GET(RetrofitConstants.DOU_UA_CALENDAR_API_URL)
    fun getEvents(): Call<ItemDataWrapper>

    @GET(RetrofitConstants.DOU_UA_VACANCIES_API_URL)
    fun getVacancies(): Call<ItemDataWrapper>

    @GET(RetrofitConstants.DOU_UA_CALENDAR_API_URL)
    fun getRawEvents(): Call<ResponseBody>

    @GET(RetrofitConstants.DOU_UA_CALENDAR_API_URL)
    fun getEventsObservable(): Observable<ItemDataWrapper>



    companion object {
        fun create(): DouAPIService {
            val retrofit = Retrofit.Builder()
                    .baseUrl(RetrofitConstants.DOU_UA_BASE_API_URL)
                    // TODO Deprecated?
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            return retrofit.create(DouAPIService::class.java)
        }
    }


}