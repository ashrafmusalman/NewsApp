package com.example.newsapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class topHeadLinesResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)