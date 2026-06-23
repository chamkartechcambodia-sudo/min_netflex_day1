package com.example.andoid.netflixmini.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.andoid.netflixmini.databinding.FragmentDetailBinding
import com.example.andoid.netflixmini.overview.OverviewViewModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    // SafeArgs: the Movie passed from OverviewFragment.
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this)[DetailViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val movie = args.selectedMovie

        binding.detailTitle.text = movie.title
        binding.detailRating.text = "★ %.1f".format(movie.voteAverage)
        binding.detailOverview.text = movie.overview

        // Show only the year (first 4 chars of "2026-04-15"); hide the dot if there is no date.
        val year = movie.releaseDate?.take(4)
        binding.detailRelease.text = year ?: ""
        binding.detailDot.visibility = if (year.isNullOrBlank()) View.GONE else View.VISIBLE

        // Prefer the wide backdrop; fall back to the poster.
        val imagePath = movie.backdropPath ?: movie.posterPath
        if (imagePath != null) {
            Glide.with(binding.detailImage.context)
                .load("https://image.tmdb.org/t/p/w780$imagePath")
                .into(binding.detailImage)
        }
        viewModel.fetchTrailer(movie.id)
        viewModel.trailerKey.observe(viewLifecycleOwner) { key ->
            val visible = if (key != null) View.VISIBLE else View.GONE
            binding.playTrailerButton.visibility = visible
            binding.heroPlayButton.visibility = visible
            if (key != null) {
                val openTrailer = View.OnClickListener {
                    val url = "https://www.youtube.com/watch?v=$key".toUri()
                    startActivity(Intent(Intent.ACTION_VIEW, url))
                }
                binding.playTrailerButton.setOnClickListener(openTrailer)
                binding.heroPlayButton.setOnClickListener(openTrailer)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}