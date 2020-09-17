package com.example.android.newsservice

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.android.newsservice.api.NewsFetch
import com.example.android.newsservice.data.NewsItem
import com.example.android.newsservice.detailview.DetailViewActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    MainActivityAdapter.CallbackItemClickListener, DialogInterface.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {
    private lateinit var mainActivityAdapter: MainActivityAdapter
    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var dialogLoading: Dialog
    private var dialogError: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        newsRecyclerView = findViewById(R.id.recyclerViewNewsList)
        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        mainActivityAdapter = MainActivityAdapter(emptyList(), this)
        newsRecyclerView.adapter = mainActivityAdapter
        dialogLoading = initLoadingDialog()

        setSpinner()

        mainActivityViewModel.newsItemLiveData.observe(
            this,
            { news ->
                updateUI(news)
            })
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

    private fun setSpinner() {
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

    private fun updateUI(newsItems: List<NewsItem>) {
        mainActivityAdapter = MainActivityAdapter(newsItems, this)
        newsRecyclerView.adapter = mainActivityAdapter

        dismissLoadingDialog(dialogLoading)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            when (parent.getItemAtPosition(position).toString()) {
                getString(R.string.spinner_error_category) -> {
                    supportActionBar?.title =
                        parent.getItemAtPosition(position).toString()
                    mainActivityViewModel.category = ""
                    mainActivityViewModel.calendar.value = Calendar.getInstance().time
                }
                else -> {
                    supportActionBar?.title =
                        parent.getItemAtPosition(position).toString()
                    mainActivityViewModel.category = parent.getItemAtPosition(position).toString()
                        .toLowerCase(Locale.getDefault())
                    mainActivityViewModel.calendar.value = Calendar.getInstance().time
                }
            }
        }
        showLoadingDialog(dialogLoading)
    }

    private fun initLoadingDialog(): Dialog {
        val builder = AlertDialog.Builder(this)
        val loadingDialogView = layoutInflater.inflate(R.layout.loading_dialog, null)
        builder.setView(loadingDialogView)
        return builder.create()
    }

    private fun showLoadingDialog(dialog: Dialog) {
        dialog.show()
    }

    private fun dismissLoadingDialog(dialog: Dialog) {
        dialog.dismiss()
        if (!NewsFetch.isResponseCorrect && dialogError == null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.error_alert_dialog_title))
            builder.setMessage(getString(R.string.error_alert_dialog_message))
            builder.setPositiveButton(getString(R.string.error_alert_dialog_button_text), this)
            dialogError = builder.create()
            dialogError?.show()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dialogError = null
        dialog?.dismiss()
    }

    override fun onRefresh() {
        swipeRefreshLayoutNews.isRefreshing = false
        showLoadingDialog(dialogLoading)
        mainActivityViewModel.calendar.value = Calendar.getInstance().time
    }
}