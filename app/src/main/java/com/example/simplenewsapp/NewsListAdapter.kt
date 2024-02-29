package com.example.simplenewsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.simplenewsapp.databinding.ListItemNewsBinding
import java.util.UUID

class NewsHolder(
    private val binding: ListItemNewsBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(news: News, onNewsClicked: (newsId: UUID) -> Unit) {
        binding.newsTitle.text = news.title
        binding.newsDate.text = news.date.toString()
        binding.newsAuthor.text = news.author

        binding.newsImg.load(news.image)

        binding.root.setOnClickListener {
            onNewsClicked(news.id)
        }
    }
}

class NewsListAdapter(
    private var news: List<News>,
    private val onNewsClicked: (newsId: UUID) -> Unit

) : RecyclerView.Adapter<NewsHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemNewsBinding.inflate(inflater, parent, false)
        return NewsHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val news = news[position]
        holder.bind(news, onNewsClicked)
    }

    fun updateNewsList(newNewsList: List<News>) {
        news = newNewsList
        notifyDataSetChanged()
    }

    override fun getItemCount() = news.size
}