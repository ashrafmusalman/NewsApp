package com.example.newsapp.ui.adapter


import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.ui.viewmodel.newsVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class savedNewsAdapter(private val context: Context, private val NewsViewModel: newsVM) :
    RecyclerView.Adapter<savedNewsAdapter.newsViewHolder>() {
    var savedNewsItemClicked: ((Article) -> Unit)? =null// this is used to handle the click event on the news item


    inner class newsViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        var newsImage = view.findViewById<ImageView>(R.id.newsImage)
        val newsTitle = view.findViewById<TextView>(R.id.tvTitle)
        val newsDescription = view.findViewById<TextView>(R.id.tvDescription)
        val newsSource = view.findViewById<TextView>(R.id.newsSource)
        val newsPublishedAt = view.findViewById<TextView>(R.id.tvPublishedAt)


    }

    private val diffUtil = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem

        }

    }

    private val asyncListDiffer =
        AsyncListDiffer(this, diffUtil)// use to perform the diffutil operation in background thread

    fun saveData(data: List<Article>) {// this function is used to save the data in the list
        asyncListDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): newsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_design, parent, false)
        return newsViewHolder(view)
    }

    override fun onBindViewHolder(holder: newsViewHolder, position: Int) {
        val news = asyncListDiffer.currentList[position]
        holder.apply {
            newsTitle.text = news.title
            newsDescription.text = news.description
            newsSource.text = news.source.toString()
            newsPublishedAt.text = news.publishedAt
            Glide.with(holder.itemView).load(news.urlToImage).placeholder(R.drawable.newsicon)
                .into(newsImage)

        }


holder.itemView.setOnClickListener{
    savedNewsItemClicked?.invoke(news)
}

        holder.itemView.setOnLongClickListener() {
            showOptionsDialog(news) // Pass the specific article to the dialog
            true
        }



    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size

    }


    private fun showOptionsDialog(article: Article) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Are You Sure To Delete News?")

        dialog.setPositiveButton("Yes") { _, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                NewsViewModel.delete(article)

            }
        }

        dialog.setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
        }


        dialog.show()
    }




    }




