package com.example.andoid.netflixmini

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.andoid.netflixmini.databinding.ActivityMainBinding
import com.example.andoid.netflixmini.overview.MoviePosterAdapter
import com.example.andoid.netflixmini.overview.OverviewViewModel
import com.example.andoid.netflixmini.overview.TmdbApiStatus

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate with View Binding, then show its root view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}