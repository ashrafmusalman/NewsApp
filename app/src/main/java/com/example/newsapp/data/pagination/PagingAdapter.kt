package com.example.newsapp.data.pagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.data.model.Article

class pagingAdapter : PagingDataAdapter<Article, pagingAdapter.pagingViewHolder>(diffUtil) {

    var onNewsItemClicked: ((Article) -> Unit)? = null

    inner class pagingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var newsImage: ImageView = view.findViewById(R.id.newsImage)
        val newsTitle: TextView = view.findViewById(R.id.tvTitle)
        val newsDescription: TextView = view.findViewById(R.id.tvDescription)
        val newsSource: TextView = view.findViewById(R.id.newsSource)
        val newsPublishedAt: TextView = view.findViewById(R.id.tvPublishedAt)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pagingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_design, parent, false)
        return pagingViewHolder(view)
    }

    override fun onBindViewHolder(holder: pagingViewHolder, position: Int) {
        val news = getItem(position)
        news?.let {
            holder.apply {
                newsTitle.text = news.title
                newsDescription.text = news.description
                newsSource.text = news.source?.name
                newsPublishedAt.text = news.publishedAt
                Glide.with(holder.itemView).load(news.urlToImage).into(newsImage)
            }

            holder.itemView.setOnClickListener {
                onNewsItemClicked?.invoke(news)
            }
        }


        }
    }

