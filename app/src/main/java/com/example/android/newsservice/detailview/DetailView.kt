package com.example.android.newsservice.detailview

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface DetailView:MvpView {
    @AddToEndSingle
    fun updateUI()
}