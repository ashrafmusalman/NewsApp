package com.example.newsapp.ui.view.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.data.db.databaseInstance
import com.example.newsapp.data.repo.NewsRepository
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.ui.viewmodel.newsVM
import com.example.newsapp.ui.viewmodel.newsViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var newsViewModel: newsVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ➡️ to get the viewmodel
     val newsDao = databaseInstance.getDatabaseInstance(this).newsDAO()
        val repo = NewsRepository(newsDao)
        newsViewModel = ViewModelProvider(this, newsViewModelFactory(repo))[newsVM::class.java]






        // ➡️change color of status bar and navigation bar
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimary)


        // ➡️ to go from one bottom navigation item to another
        val supportfragmentManager =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = supportfragmentManager.navController
        binding.bottomNavigation.setupWithNavController(navController)

    }


}