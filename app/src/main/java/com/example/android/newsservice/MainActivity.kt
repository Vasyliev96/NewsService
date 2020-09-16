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
import com.example.android.newsservice.detailview.DetailViewActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.article_list_item.*
import java.util.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    MainActivityAdapter.CallbackItemClickListener, DialogInterface.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {
    private lateinit var mainActivityAdapter: MainActivityAdapter
    private val mainActivityViewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }
    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var dialog: Dialog
    private var dialogError: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        newsRecyclerView = findViewById(R.id.recyclerViewNewsList)
        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        mainActivityAdapter = MainActivityAdapter(emptyList(), this)
        newsRecyclerView.adapter = mainActivityAdapter
        dialog = initLoadingDialog()

        setSpinner()

        mainActivityViewModel.newsItemLiveData.observe(
            this,
            { news ->
                updateUI(news)
            })
        swipeRefreshLayoutNews.setOnRefreshListener(this)
    }

    override fun onItemClick(position: Int) {
        DetailViewActivityViewModel.newsItem = mainActivityAdapter.getItem(position)
        startActivity(
            Intent(this, DetailViewActivity::class.java),
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity,
                textViewAuthor,
                "author"
            ).toBundle()
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

        dismissLoadingDialog(dialog)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            when (parent.getItemAtPosition(position).toString()) {
                "error" -> {
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
        showLoadingDialog(dialog)
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
        showLoadingDialog(dialog)
        mainActivityViewModel.calendar.value = Calendar.getInstance().time
    }
}