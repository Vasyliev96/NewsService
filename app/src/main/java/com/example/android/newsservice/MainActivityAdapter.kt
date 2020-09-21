package com.example.android.newsservice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.newsservice.data.NewsItem
import kotlinx.android.synthetic.main.article_list_item.view.*

class MainActivityAdapter(var news: List<NewsItem>, private val context: Context) :
    RecyclerView.Adapter<MainActivityAdapter.NewsItemViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(this.context)
    private val callbackItemClickListener: CallbackItemClickListener =
        context as CallbackItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val view = inflater.inflate(R.layout.article_list_item, parent, false)
        return NewsItemViewHolder(view)
    }

    override fun getItemCount() = news.size

    override fun onBindViewHolder(holderNewsItem: NewsItemViewHolder, position: Int) {
        holderNewsItem.bind(news[position])
    }

    fun getItem(position: Int): NewsItem {
        return news[position]
    }

    inner class NewsItemViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(newsItem: NewsItem) {
            if (newsItem.author == null || newsItem.author == "") {
                itemView.textViewAuthor.text = context.getString(R.string.anonymous_author)
            } else {
                itemView.textViewAuthor.text = newsItem.author
            }
            itemView.textViewTitle.text = newsItem.title ?: context.getString(R.string.empty_title)
            itemView.textViewPublishedAt.text = newsItem.publishedAt
        }

        override fun onClick(view: View?) {
            callbackItemClickListener.onItemClick(adapterPosition)
        }
    }

    interface CallbackItemClickListener {
        fun onItemClick(position: Int)
    }
}
