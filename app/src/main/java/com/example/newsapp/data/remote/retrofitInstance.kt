package com.example.newsapp.data.remote

import com.example.newsapp.util.constant.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class retrofitInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }

        val retrofitobject by lazy {// this is used to get the retrofit object
            retrofit.create(newsInterface::class.java)
        }
    }


}