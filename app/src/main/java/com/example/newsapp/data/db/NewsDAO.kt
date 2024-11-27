package com.example.newsapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newsapp.data.model.Article

@Dao
interface NewsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: Article)

    @Update
    suspend fun update(news: Article)

    @Query("SELECT * FROM news_table")
    fun getAllNews(): LiveData<List<Article>>


    @Delete
    suspend fun delete(article: Article)

    //check item based on the title and return boolean
    @Query("SELECT EXISTS (SELECT 1 FROM news_table WHERE title = :title)")
    fun isItemExists(title: String): Boolean






}