package com.unagit.douuajobsevents.models

import com.unagit.douuajobsevents.helpers.RetrofitConstants
import retrofit2.Call
import retrofit2.http.GET

interface DouAPIService {
    @GET(RetrofitConstants.DOU_UA_CALENDAR_API_URL)
    fun getEvents(): Call<ItemDataWrapper>

    @GET(RetrofitConstants.DOU_UA_VACANCIES_API_URL)
    fun getVacancies(): Call<ItemDataWrapper>


}