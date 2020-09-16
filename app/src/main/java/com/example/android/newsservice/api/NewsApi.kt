package com.example.android.newsservice.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("everything?sortBy=publishedAt&apiKey=e0ca0026e439464691f5576227b6940c")
    fun fetchNews(@Query("qInTitle") query: String, @Query("from") date: String): Call<NewsResponse>
}