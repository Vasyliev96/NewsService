package com.example.android.newsservice

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MainView : MvpView {
    @AddToEndSingle
    fun setTitle()

    @AddToEndSingle
    fun setSpinner()

    @AddToEndSingle
    fun updateUI()

    @AddToEndSingle
    fun showLoadingDialog()

    @AddToEndSingle
    fun dismissLoadingDialog()

    @AddToEndSingle
    fun showErrorDialog()

    @AddToEndSingle
    fun dismissErrorDialog()
}