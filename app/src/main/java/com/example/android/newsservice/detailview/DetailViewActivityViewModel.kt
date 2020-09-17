package com.example.android.newsservice.detailview

import androidx.lifecycle.ViewModel
import com.example.android.newsservice.data.NewsItem
import com.example.android.newsservice.data.SourceName

class DetailViewActivityViewModel : ViewModel() {
    var newsItem: NewsItem? = null

    fun setNews(sourceName: String?, author: String?, description: String?, urlToImage: String?) {
        newsItem =
            NewsItem(SourceName(sourceName), author, null, description, null, urlToImage, null)
    }
}