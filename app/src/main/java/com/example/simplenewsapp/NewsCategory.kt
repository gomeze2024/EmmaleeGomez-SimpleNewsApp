package com.example.simplenewsapp

sealed class NewsCategory {
    data object Business : NewsCategory()
    data object Entertainment : NewsCategory()
    data object General : NewsCategory()
    data object Health : NewsCategory()
    data object Science : NewsCategory()
    data object Sports : NewsCategory()
    data object Technology : NewsCategory()
}