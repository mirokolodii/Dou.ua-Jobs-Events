package com.unagit.douuajobsevents.models

import com.unagit.douuajobsevents.helpers.RetrofitConstants
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET

interface DouAPIService {
    @GET(RetrofitConstants.DOU_UA_CALENDAR_API_URL)
    fun getEvents(): Call<ItemDataWrapper>

    @GET(RetrofitConstants.DOU_UA_VACANCIES_API_URL)
    fun getVacancies(): Call<ItemDataWrapper>

    @GET(RetrofitConstants.DOU_UA_CALENDAR_API_URL)
    fun getRawEvents(): Call<ResponseBody>


    companion object {
        fun create(): DouAPIService {
            val retrofit = Retrofit.Builder()
                    .baseUrl(RetrofitConstants.DOU_UA_BASE_API_URL)
//                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build()
            return retrofit.create(DouAPIService::class.java)
        }
    }


}