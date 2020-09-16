package com.example.android.newsservice

import androidx.lifecycle.*
import com.example.android.newsservice.api.NewsFetch
import com.example.android.newsservice.data.NewsItem
import java.text.SimpleDateFormat
import java.util.*

class MainActivityViewModel : ViewModel() {
    var calendar = MutableLiveData<Date>().also { it.value = Calendar.getInstance().time }
    private val myDateFormat = "yyyy-MM-dd"
    private val dateFormat = SimpleDateFormat(myDateFormat, Locale.US)
    var category = "software"
    val newsItemLiveData: LiveData<List<NewsItem>> =
        Transformations.switchMap(calendar) { date ->
            NewsFetch.fetchNews(category, dateFormat.format(date))
        }
}