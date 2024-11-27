package com.example.newsapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity("news_table")
@Parcelize
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    @PrimaryKey
    val title: String,
    val url: String?,
    val urlToImage: String?
):Parcelable//✅✅✅ since the source is not primitive type so if we write parallelize here it wont support so we need  to
// make the source class as parcelable