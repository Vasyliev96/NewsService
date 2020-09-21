package com.example.android.newsservice.api

import com.example.android.newsservice.MainPresenter
import com.example.android.newsservice.data.NewsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsFetch {
    private val newsApi: NewsApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newsApi = retrofit.create(NewsApi::class.java)
    }

    fun fetchNews(
        query: String,
        date: String,
        presenter: MainPresenter
    ){
        val newsApiRequest: Call<NewsResponse> = newsApi.fetchNews(query, date)
        newsApiRequest.enqueue(object : Callback<NewsResponse> {
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                presenter.requestFailed()

            }

            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                if (response.body() != null) {
                    val newsResponse: NewsResponse? = response.body()
                    val newsItems: List<NewsItem> = newsResponse?.newsItems ?: mutableListOf()
                    presenter.setNews(newsItems)
                } else {
                    presenter.requestFailed()
                }
            }
        })
    }
}
