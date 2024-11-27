package com.example.newsapp.data.pagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R

class loaderAdapter: LoadStateAdapter<loaderAdapter.LoaderViewHolder>() {
    class LoaderViewHolder(itemview:View):RecyclerView.ViewHolder(itemview){
        val progress=itemview.findViewById<ProgressBar>(R.id.progress)
        fun Bind(loadState: LoadState){
            progress.isVisible=loadState is LoadState.Loading

        }


    }

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.Bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.loader,parent,false)
        return LoaderViewHolder(view)
    }
}