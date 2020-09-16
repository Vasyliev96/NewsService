package com.example.android.newsservice.detailview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.android.newsservice.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_view.*

class DetailViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view)
        updateUI()
    }

    private fun updateUI() {
        if (DetailViewActivityViewModel.newsItem?.author == null || DetailViewActivityViewModel.newsItem?.author == "") {
            textViewAuthor.text = getString(R.string.anonymous_author)
        } else {
            textViewAuthor.text = DetailViewActivityViewModel.newsItem?.author
        }
        textViewSourceName.text =
            DetailViewActivityViewModel.newsItem?.source?.name ?: getString(R.string.empty_source_name)
        textViewDescription.text =
            DetailViewActivityViewModel.newsItem?.description ?: getString(R.string.empty_description)
        textViewGlide.text = getString(
            R.string.activity_article_details_text_view_glide_text,
            DetailViewActivityViewModel.newsItem?.urlToImage ?: getString(R.string.empty_url_to_image)
        )
        Glide.with(this).load(DetailViewActivityViewModel.newsItem?.urlToImage).into(imageViewGlide)
        textViewPicasso.text = getString(
            R.string.activity_article_details_text_view_picasso_text,
            DetailViewActivityViewModel.newsItem?.urlToImage ?: getString(R.string.empty_url_to_image)
        )
        Picasso.get().load(DetailViewActivityViewModel.newsItem?.urlToImage).into(imageViewPicasso)
    }
}