package com.example.simplenewsapp
import android.app.Application

class NewsApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        NewsRepository.initialize(this)
    }
}