package com.example.newsapp.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.ui.adapter.newsAdapter
import com.example.newsapp.ui.adapter.savedNewsAdapter
import com.example.newsapp.ui.view.activities.MainActivity
import com.example.newsapp.ui.viewmodel.newsVM
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var newsViewModel: newsVM
    private lateinit var adapter: savedNewsAdapter
    private lateinit var news: List<Article>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedNewsBinding.bind(view)
        newsViewModel = (activity as MainActivity).newsViewModel
        adapter=savedNewsAdapter(requireContext(),newsViewModel)

        news = ArrayList()
        goToArticleFragmentFromSavedFragment()
        setUprecyclerview()
        setDataIntoAdapter()
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvSaved)
    }


    private fun setDataIntoAdapter() {
        newsViewModel.getAllNews().observe(viewLifecycleOwner) {articles->
            binding.tvSavedNewsInvisible.visibility = if (articles.isEmpty()) View.VISIBLE else View.GONE
            news=articles
            adapter.saveData(articles)

        }
    }


    private fun setUprecyclerview() {


        binding.rvSaved.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvSaved.adapter = adapter

    }




    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }


        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val position = viewHolder.adapterPosition // Find the position of the item
            val item = news[position] // Find the item based on the position
            lifecycleScope.launch(Dispatchers.IO) {
                newsViewModel.delete(item)
                withContext(Dispatchers.Main) {
                    adapter.notifyItemRemoved(position)


                }


            }
        }

    }


    private fun goToArticleFragmentFromSavedFragment() {

        adapter.savedNewsItemClicked = { article ->
            val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(article)
            findNavController().navigate(action)
        }


    }






}