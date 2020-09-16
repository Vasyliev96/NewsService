package com.example.android.newsservice.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.newsservice.data.NewsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsFetch {
    var isResponseCorrect = false
    private val newsApi: NewsApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsApi = retrofit.create(NewsApi::class.java)
    }

    fun fetchNews(query: String, date: String): LiveData<List<NewsItem>> {
        val responseLiveData: MutableLiveData<List<NewsItem>> = MutableLiveData()
        val newsApiRequest: Call<NewsResponse> = newsApi.fetchNews(query, date)
        newsApiRequest.enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                isResponseCorrect = false
                responseLiveData.value = mutableListOf()
            }

            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                isResponseCorrect = response.body() != null
                val newsResponse: NewsResponse? = response.body()
                val newsItems: List<NewsItem> = newsResponse?.newsItems ?: mutableListOf()
                responseLiveData.value = newsItems
            }
        })
        return responseLiveData
    }
}
