package com.example.android.newsservice.detailview

import androidx.lifecycle.ViewModel
import com.example.android.newsservice.data.NewsItem

class DetailViewActivityViewModel:ViewModel() {
    companion object{
        var newsItem: NewsItem?=null
    }
}