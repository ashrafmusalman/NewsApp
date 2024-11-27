package com.example.newsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.data.repo.NewsRepository

class newsViewModelFactory(private val repo: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(newsVM::class.java)) {
            return newsVM(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }

}


