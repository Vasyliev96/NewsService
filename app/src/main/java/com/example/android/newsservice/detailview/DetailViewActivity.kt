package com.example.android.newsservice.detailview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.android.newsservice.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_view.*

class DetailViewActivity : AppCompatActivity() {

    private val detailViewActivityViewModel by lazy {
        ViewModelProvider(this).get(DetailViewActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view)

        detailViewActivityViewModel.setNews(
            intent.getStringExtra(NEWS_ITEM_SOURCE_NAME),
            intent.getStringExtra(NEWS_ITEM_AUTHOR),
            intent.getStringExtra(NEWS_ITEM_DESCRIPTION),
            intent.getStringExtra(NEWS_ITEM_URL_TO_IMAGE)
        )

        updateUI()
    }

    private fun updateUI() {
        if (detailViewActivityViewModel.newsItem?.author == null || detailViewActivityViewModel.newsItem?.author == "") {
            textViewAuthor.text = getString(R.string.anonymous_author)
        } else {
            textViewAuthor.text = detailViewActivityViewModel.newsItem?.author
        }
        textViewSourceName.text =
            detailViewActivityViewModel.newsItem?.source?.name
                ?: getString(R.string.empty_source_name)
        textViewDescription.text =
            detailViewActivityViewModel.newsItem?.description
                ?: getString(R.string.empty_description)
        textViewGlide.text = getString(
            R.string.activity_article_details_text_view_glide_text,
            detailViewActivityViewModel.newsItem?.urlToImage
                ?: getString(R.string.empty_url_to_image)
        )
        Glide.with(this).load(detailViewActivityViewModel.newsItem?.urlToImage).into(imageViewGlide)
        textViewPicasso.text = getString(
            R.string.activity_article_details_text_view_picasso_text,
            detailViewActivityViewModel.newsItem?.urlToImage
                ?: getString(R.string.empty_url_to_image)
        )
        Picasso.get().load(detailViewActivityViewModel.newsItem?.urlToImage).into(imageViewPicasso)
    }

    companion object {
        const val NEWS_ITEM_AUTHOR = "com.example.android.newsservice.news_item_author"
        const val NEWS_ITEM_SOURCE_NAME = "com.example.android.newsservice.news_item_source_name"
        const val NEWS_ITEM_DESCRIPTION = "com.example.android.newsservice.news_item_description"
        const val NEWS_ITEM_URL_TO_IMAGE = "com.example.android.newsservice.news_item_url"
    }
}