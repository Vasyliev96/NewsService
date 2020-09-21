package com.example.android.newsservice

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.android.newsservice.detailview.DetailViewActivity
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), AdapterView.OnItemSelectedListener,
    MainActivityAdapter.CallbackItemClickListener, DialogInterface.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener, MainView {
    private lateinit var mainActivityAdapter: MainActivityAdapter
    private val mainPresenter by moxyPresenter { MainPresenter() }
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var dialogLoading: Dialog
    private lateinit var dialogError: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        newsRecyclerView = findViewById(R.id.recyclerViewNewsList)
        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        mainActivityAdapter = MainActivityAdapter(emptyList(), this)
        dialogLoading = initLoadingDialog()
        dialogError = initErrorDialog()
        swipeRefreshLayoutNews.setOnRefreshListener(this)
    }

    override fun onItemClick(position: Int) {
        val newsItem = mainActivityAdapter.getItem(position)
        startActivity(
            Intent(this, DetailViewActivity::class.java).apply {
                putExtra(
                    DetailViewActivity.NEWS_ITEM_AUTHOR,
                    newsItem.author
                )
                putExtra(
                    DetailViewActivity.NEWS_ITEM_SOURCE_NAME,
                    newsItem.source.name
                )
                putExtra(
                    DetailViewActivity.NEWS_ITEM_DESCRIPTION,
                    newsItem.description
                )
                putExtra(
                    DetailViewActivity.NEWS_ITEM_URL_TO_IMAGE,
                    newsItem.urlToImage
                )
            },
            newsRecyclerView.layoutManager?.findViewByPosition(position)?.let {
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@MainActivity,
                    it,
                    getString(R.string.transition_name_author)
                ).toBundle()
            }
        )
    }

    override fun setSpinner() {
        spinnerCategories.onItemSelectedListener = this
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.news_categories)
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategories.adapter = adapter
        }
    }

    override fun updateUI() {
        mainActivityAdapter.news = mainPresenter.newsItems
        newsRecyclerView.adapter = mainActivityAdapter
    }

    override fun setTitle() {
        supportActionBar?.title = mainPresenter.category
    }

    override fun showLoadingDialog() {
        dialogLoading.show()
    }

    override fun dismissLoadingDialog() {
        dialogLoading.dismiss()
    }

    override fun showErrorDialog() {
        dialogError.show()
    }

    override fun dismissErrorDialog() {
        dialogError.dismiss()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            mainPresenter.updateUI(parent.getItemAtPosition(position).toString())
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        mainPresenter.dismissErrorDialog()
    }

    override fun onRefresh() {
        swipeRefreshLayoutNews.isRefreshing = false
        mainPresenter.updateUI(mainPresenter.category)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun initLoadingDialog(): Dialog {
        val builder = AlertDialog.Builder(this)
        val loadingDialogView = layoutInflater.inflate(R.layout.loading_dialog, null)
        builder.setView(loadingDialogView)
        return builder.create()
    }

    private fun initErrorDialog(): Dialog {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.error_alert_dialog_title))
        builder.setMessage(getString(R.string.error_alert_dialog_message))
        builder.setPositiveButton(getString(R.string.error_alert_dialog_button_text), this)
        return builder.create()
    }
}