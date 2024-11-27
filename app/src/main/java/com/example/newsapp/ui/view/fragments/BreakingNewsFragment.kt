package com.example.newsapp.ui.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.data.pagination.loaderAdapter
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.ui.view.activities.MainActivity
import com.example.newsapp.ui.viewmodel.newsVM
import com.example.newsapp.data.pagination.pagingAdapter
import kotlinx.coroutines.launch

class breakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private lateinit var binding: FragmentBreakingNewsBinding
    private lateinit var newsViewModel: newsVM
    private lateinit var adapter: pagingAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)
        newsViewModel = (activity as MainActivity).newsViewModel
        adapter = pagingAdapter()
        binding.progressCircular.visibility = View.VISIBLE
        setupRecyclerView()
        observeNewsData()
        goToArticleFragmentFromBreakingFragment()
    }

    private fun setupRecyclerView() {

        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticle.setHasFixedSize(true)
        binding.rvArticle.adapter = adapter.withLoadStateHeaderAndFooter(
            header = loaderAdapter(),
            footer = loaderAdapter()
        )
    }

    private fun observeNewsData() {
        lifecycleScope.launch {

            newsViewModel.pagedTopHeadlines.observe(viewLifecycleOwner) {
                binding.progressCircular.visibility = View.GONE
                adapter.submitData(lifecycle, it)
            }


        }
    }



    fun goToArticleFragmentFromBreakingFragment(){
        adapter.onNewsItemClicked={
            val action=breakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }
    }


}






