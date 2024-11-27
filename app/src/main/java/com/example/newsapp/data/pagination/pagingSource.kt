// NewsPagingSource.kt
package com.example.newsapp.data.repo

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.remote.newsInterface
import com.example.newsapp.util.constant.Companion.API_KEY
import retrofit2.HttpException
import java.io.IOException

class NewsPagingSource(
    private val apiService: newsInterface,
    private val category: String,
    private val country: String = "us"
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        Log.d("PagingSource", "Loading page  with page size ${params.loadSize}")

        return try {
            val response = apiService.getTopHeadlines(country, API_KEY, page, category)
            val pagedResponse = response.body() // get the data from the response body
            val articles=pagedResponse?.articles?: emptyList()// Extract the list of articles from the response

            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (articles.isEmpty()) null else page + 1

            )


        }
        catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
        catch (e:Exception){
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}
