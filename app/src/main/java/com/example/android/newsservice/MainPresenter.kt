package com.example.android.newsservice

import com.example.android.newsservice.api.NewsFetch
import com.example.android.newsservice.data.NewsItem
import moxy.MvpPresenter
import java.text.SimpleDateFormat
import java.util.*

class MainPresenter : MvpPresenter<MainView>() {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    var category = "Software"
    var newsItems: List<NewsItem> =
        emptyList()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setTitle()
        viewState.showLoadingDialog()
        viewState.setSpinner()
    }

    fun updateUI(category: String) {
        viewState.showLoadingDialog()
        this.category = category
        viewState.setTitle()
        when (category) {
            "error" -> {
                NewsFetch.fetchNews(
                    "",
                    dateFormat.format(Calendar.getInstance().time),
                    this@MainPresenter
                )
            }
            else -> {
                NewsFetch.fetchNews(
                    category,
                    dateFormat.format(Calendar.getInstance().time),
                    this@MainPresenter
                )
            }
        }
    }

    fun setNews(newsItems: List<NewsItem>) {
        this.newsItems = newsItems
        viewState.updateUI()
        viewState.dismissLoadingDialog()
    }

    fun requestFailed() {
        newsItems = emptyList()
        viewState.updateUI()
        viewState.dismissLoadingDialog()
        viewState.showErrorDialog()
    }

    fun dismissErrorDialog() {
        viewState.dismissErrorDialog()
    }
}