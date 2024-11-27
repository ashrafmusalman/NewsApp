package com.example.newsapp.data.remote

import com.example.newsapp.data.model.topHeadLinesResponse
import com.example.newsapp.util.constant
import com.example.newsapp.util.constant.Companion.API_KEY
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface newsInterface {


    //for the top-headlines
    @GET("top-headlines")//it allows to give data fo Top headline only
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("page") page: Int,
        @Query("category") category: String,
        ): Response<topHeadLinesResponse>

    //it gives data to everythng present in that apikey
    @GET("everything")
    fun searchNews(
        @Query("q") searchQuery: String,
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("page") page: Int = 1,
    ): Call<topHeadLinesResponse>



}
