package com.example.newsapp.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.ui.adapter.newsAdapter
import com.example.newsapp.ui.view.activities.MainActivity
import com.example.newsapp.ui.viewmodel.newsVM


class searchNewsFragment : Fragment(R.layout.fragment_search_news) {
    private lateinit var binding: FragmentSearchNewsBinding
    private lateinit var adapter: newsAdapter

    lateinit var newsViewModel: newsVM
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchNewsBinding.bind(view)
        newsViewModel = (activity as MainActivity).newsViewModel
        adapter = newsAdapter()
        setUpRV()

        binding.tvSearchInvisible.visibility=View.VISIBLE

        newsViewModel.searchData.observe(viewLifecycleOwner) { searchResults ->
            binding.progressCircular.visibility = View.GONE
            binding.tvSearchInvisible.visibility=View.GONE

            if (searchResults != null && searchResults.isNotEmpty()) {

                adapter.saveData(searchResults)
            } else {

                adapter.saveData(emptyList()) // Clear the adapter
            }
        }

        binding.go.setOnClickListener {
            perfomSearch()
        }

        gotoArticleFragmentFromSearchFragment()
        searchFromMobileKepadSearchIcon()




    }

    private fun perfomSearch() {
        binding.progressCircular.visibility = View.VISIBLE

        val query = binding.EtSearch.text.toString()
        if (query.isNotEmpty()) {
            Log.d("query", query)
            newsViewModel.getSearchData(query)
        }

    }


    fun setUpRV() {
        binding.rvSearch.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvSearch.adapter = adapter
    }


    fun gotoArticleFragmentFromSearchFragment() {
        adapter.onNewsItmeClicked = {
            val action=searchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }

    }

    private fun searchFromMobileKepadSearchIcon() {


//perform search wen the mobile numpad search icon is clicked
        binding.EtSearch.setOnEditorActionListener { _, actionId, event ->
            // Check if the action is "search" or "go"
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_GO ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                perfomSearch()
                true


            } else {
                false
            }

        }

    }




}