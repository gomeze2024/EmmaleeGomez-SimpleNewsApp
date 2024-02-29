package com.example.simplenewsapp

import java.util.Date
import java.util.UUID
import androidx.room.PrimaryKey

data class News(
    @PrimaryKey val id: UUID,
    val title: String,
    val image: String,
    val date: Date,
    val author: String,
    val content: String,
    val url: String
)