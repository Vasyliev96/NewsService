package com.example.android.newsservice.data

import com.example.android.newsservice.data.SourceName

data class NewsItem(
    val source: SourceName,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null
)