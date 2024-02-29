package com.example.simplenewsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.simplenewsapp.databinding.FragmentNewsDetailBinding
import kotlinx.coroutines.launch

class NewsDetailFragment : Fragment() {

    private var _binding: FragmentNewsDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: NewsDetailFragmentArgs by navArgs()

    private val newsDetailViewModel: NewsDetailViewModel by viewModels {
        NewsDetailViewModelFactory(args.newsId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsDetailViewModel.news.collect { news ->
                    news?.let { updateUi(it) }
                }
            }
        }
    }

    private fun updateUi(news: News) {
        binding.apply {
            newsTitle.text = news.title
            newsDetails.text = news.date.toString()
            newsAuthor.text = news.author
            newsContent.text = news.content
            newsUrl.text = news.url
            newsImg.load(news.image)

        }
    }
}
