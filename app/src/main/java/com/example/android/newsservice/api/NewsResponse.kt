package com.example.android.newsservice.api

import com.example.android.newsservice.data.NewsItem
import com.google.gson.annotations.SerializedName

class NewsResponse {
    @SerializedName("articles")
    val newsItems: List<NewsItem>? = null
}