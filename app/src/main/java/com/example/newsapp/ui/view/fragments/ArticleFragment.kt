package com.example.newsapp.ui.view.fragments

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.ui.view.activities.MainActivity
import com.example.newsapp.ui.viewmodel.newsVM

class articleFragment : Fragment(R.layout.fragment_article) {
    private val args: articleFragmentArgs by navArgs()
    private lateinit var binding: FragmentArticleBinding
    private lateinit var newsViewModel: newsVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)
        newsViewModel = (activity as MainActivity).newsViewModel

        val article = args.article
        binding.progressCircular.visibility = View.VISIBLE
        // Initially, check if the item exists in the database
        newsViewModel.checkIfItemExist(article!!.title)
        // Observe the item's saved state in the database
        newsViewModel.checkTitle.observe(viewLifecycleOwner) { isItemSaved ->
            //here we will just see if item is in data base or not if yes we will give red color and if not we will give white color to heart
            updateFabColor(isItemSaved)
            binding.progressCircular.visibility = View.INVISIBLE
        }



        // Set the FAB click listener for save/delete actions
        binding.fab.setOnClickListener {
            newsViewModel.checkTitle.value?.let { isItemSaved ->//Using .value here is a way to ensure an immediate decision within the
                // click event based on the latest available data, without relying
                // solely on the observer to refresh and re-render the UI. It’s especially
                // helpful in click listeners where you need to make a quick decision based on the
                // most recent value of LiveData.
                if (isItemSaved) {
                    // If the item is saved, delete it from the database
                    newsViewModel.delete(article)
                    Toast.makeText(requireContext(), "News Deleted", Toast.LENGTH_SHORT).show()
                } else {
                    // If the item is not saved, add it to the database
                    newsViewModel.insert(article)
                    Toast.makeText(requireContext(), "News Saved Successfully", Toast.LENGTH_SHORT).show()
                }
                // Trigger a check to update the LiveData after an operation
                newsViewModel.checkIfItemExist(article.title)
            }
        }

        // WebView setup to load the article URL
        binding.webView.apply {
            webViewClient = object : android.webkit.WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    binding.progressCircular.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.progressCircular.visibility = View.INVISIBLE
                }
            }
            article.url?.let { loadUrl(it) }
        }
    }

    // Function to update FAB color based on saved state
    private fun updateFabColor(isItemSaved: Boolean) {
        val color = if (isItemSaved) {
            ContextCompat.getColor(requireContext(), R.color.red) // Red if saved
        } else {
            ContextCompat.getColor(requireContext(), R.color.white) // White if not saved
        }
        binding.fab.backgroundTintList = ColorStateList.valueOf(color)
    }
}
