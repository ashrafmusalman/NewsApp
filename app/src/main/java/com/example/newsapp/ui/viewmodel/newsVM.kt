package com.example.newsapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.repo.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class newsVM(val repository: NewsRepository) : ViewModel() {

    val searchData: LiveData<List<Article>> = repository.searchResults

    private val _checktitle = MutableLiveData<Boolean>()
    val checkTitle: LiveData<Boolean> = _checktitle

    val pagedTopHeadlines = repository.getPagedTopHeadLines().cachedIn(viewModelScope)





    fun getSearchData(querry: String) {

        repository.searchNewsOfEverything(querry)
    }


    fun insert(article: Article) = viewModelScope.launch {

        repository.insert(article)
    }

    fun getAllNews(): LiveData<List<Article>> {
        return repository.getAllNews()
    }

    fun delete(article: Article) {
        viewModelScope.launch {
            repository.delete(article)
        }
    }


    fun checkIfItemExist(title: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.checkTitle(title)
            }
            _checktitle.value = result
        }
    }

}