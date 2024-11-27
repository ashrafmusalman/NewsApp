    package com.example.newsapp.data.repo

    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.paging.Pager
    import androidx.paging.PagingConfig
    import androidx.paging.PagingData
    import androidx.paging.cachedIn
    import androidx.paging.liveData
    import com.example.newsapp.data.db.NewsDAO
    import com.example.newsapp.data.model.Article

    import com.example.newsapp.data.model.topHeadLinesResponse
    import com.example.newsapp.data.remote.retrofitInstance
    import com.example.newsapp.util.constant.Companion.API_KEY
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.launch
    import retrofit2.Call
    import retrofit2.Callback
    import retrofit2.Response

    class NewsRepository(
        private val newsDao: NewsDAO,
    // we don't need to pass the interface class as the interface class is accessed in the retrofit instance class and there we
        // have used companion object that allow direct accessibility

    ) {

        //for the top-headlines
        private val mutableNews = MutableLiveData<List<Article>>()
        val topHeadlines: LiveData<List<Article>> = mutableNews

        // for search news
        private val mutableSearchNews = MutableLiveData<List<Article>>()
        val searchResults: LiveData<List<Article>> = mutableSearchNews

        //to check whether the title is present in the database or not



        fun searchNewsOfEverything(querry: String) {
            retrofitInstance.retrofitobject.searchNews(querry)
                .enqueue(object : Callback<topHeadLinesResponse> {
                    override fun onResponse(
                        call: Call<topHeadLinesResponse>,
                        response: Response<topHeadLinesResponse>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val newsList =
                                response.body()!!.articles//The result is that newsList now holds a List<Article> if the response is successful, containing the articles from the API response.
                            Log.d("newsList", newsList.toString())
                            mutableSearchNews.value = newsList
                        } else {
                            Log.d("newsList", "null")
                        }
                    }

                    override fun onFailure(call: Call<topHeadLinesResponse>, t: Throwable) {
                        t.localizedMessage?.let { Log.d("failure", it.toString()) }
                    }

                })

        }

        // for saving when clicked on heart
        fun checkTitle(title: String): Boolean {
            return newsDao.isItemExists(title)
        }

        // for database operations
        suspend fun insert(article: Article) {
            newsDao.insert(article)

        }

        suspend fun update(article: Article) {
            newsDao.update(article)
        }

        fun getAllNews(): LiveData<List<Article>> {
            return newsDao.getAllNews()
        }

        suspend fun delete(article: Article) = newsDao.delete(article)


        fun getPagedTopHeadLines(category: String = "science")=
             Pager(
                config = PagingConfig(pageSize = 10, maxSize = 30, initialLoadSize = 10, prefetchDistance = 1, enablePlaceholders = false),
                pagingSourceFactory = { NewsPagingSource(retrofitInstance.retrofitobject, category) }
            ).liveData





    }