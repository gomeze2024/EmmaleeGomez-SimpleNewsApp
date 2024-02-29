package com.example.simplenewsapp

import NewsListViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplenewsapp.databinding.FragmentNewsListBinding
import kotlinx.coroutines.launch


private const val TAG = "NewsFragment"

class NewsListFragment : Fragment() {

    private var _binding: FragmentNewsListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val newsListViewModel: NewsListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)

        binding.newsRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsListViewModel.news.collect { news ->
                    binding.newsRecyclerView.adapter =
                        NewsListAdapter(news) { newsId ->
                            findNavController().navigate(
                                NewsListFragmentDirections.showNewsDetail(newsId)
                            )
                        }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_news_list, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.business, R.id.entertainment, R.id.general, R.id.health,
            R.id.science, R.id.sports, R.id.technology -> {
                val category = getCategoryFromMenuItem(item.itemId)
                newsListViewModel.updateNews(category)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getCategoryFromMenuItem(itemId: Int): NewsCategory {
        return when (itemId) {
            R.id.business -> NewsCategory.Business
            R.id.entertainment -> NewsCategory.Entertainment
            R.id.general -> NewsCategory.General
            R.id.health -> NewsCategory.Health
            R.id.science -> NewsCategory.Science
            R.id.sports -> NewsCategory.Sports
            R.id.technology -> NewsCategory.Technology
            else -> throw IllegalArgumentException("Invalid menu item id: $itemId")
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
