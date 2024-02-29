package com.example.simplenewsapp.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private const val API_KEY: String = BuildConfig.API_KEY

class NewsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest : Request = chain.request()
        val newUrl : HttpUrl = originalRequest.url.newBuilder()
            .addQueryParameter("country", "us")
            .addQueryParameter("apiKey", API_KEY)
            .build()

        val newRequest : Request = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }
}