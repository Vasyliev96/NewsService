package com.example.android.newsservice.detailview

import com.example.android.newsservice.data.NewsItem
import com.example.android.newsservice.data.SourceName
import moxy.MvpPresenter

class DetailPresenter : MvpPresenter<DetailView>() {
    var newsItem: NewsItem? = null
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.updateUI()
    }

    fun setNews(sourceName: String?, author: String?, description: String?, urlToImage: String?) {
        newsItem =
            NewsItem(SourceName(sourceName), author, null, description, null, urlToImage, null)
    }
}