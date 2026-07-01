package com.example.andoid.netflixmini.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.andoid.netflixmini.databinding.FragmentOverviewBinding
import com.example.andoid.netflixmini.network.Movie

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this)[OverviewViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)


        val onMovieClicked: (Movie) -> Unit = { movie ->
            findNavController().navigate(
                OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(movie)
            )
        }

        val popularAdapter = MoviePosterAdapter(onMovieClicked)
        val topRatedAdapter = MoviePosterAdapter(onMovieClicked)
        val nowPlayingAdapter = MoviePosterAdapter(onMovieClicked)
        val upcomingAdapter = MoviePosterAdapter(onMovieClicked)

        binding.popularRow.adapter = popularAdapter
        binding.topRatedRow.adapter = topRatedAdapter
        binding.nowPlayingRow.adapter = nowPlayingAdapter
        binding.upcomingRow.adapter = upcomingAdapter

        setUpRow(binding.popularRow, popularAdapter)
        setUpRow(binding.topRatedRow, topRatedAdapter)
        setUpRow(binding.nowPlayingRow, nowPlayingAdapter)
        setUpRow(binding.upcomingRow, upcomingAdapter)




        viewModel.popularMovies.observe(viewLifecycleOwner) { movies ->
            popularAdapter.submitList(movies)
        }
        viewModel.topRatedMovies.observe(viewLifecycleOwner) { movies ->
            topRatedAdapter.submitList(movies)
        }
        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) { movies ->
            nowPlayingAdapter.submitList(movies)
        }
        viewModel.upcomingMovies.observe(viewLifecycleOwner) { movies ->
            upcomingAdapter.submitList(movies)
        }
        viewModel.featured.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {
                Glide.with(binding.heroImage.context)
                    .load("https://image.tmdb.org/t/p/w780${movie.backdropPath}")
                    .into(binding.heroImage)
                binding.heroTitle.text = movie.title

                binding.heroContainer.setOnClickListener { onMovieClicked(movie) }
            }
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.loadingSpinner.visibility =
                if (status == TmdbApiStatus.LOADING) View.VISIBLE else View.GONE
            binding.errorImage.visibility =
                if (status == TmdbApiStatus.ERROR) View.VISIBLE else View.GONE
        }
        return binding.root
    }

    private fun setUpRow(rv: RecyclerView, adapter: MoviePosterAdapter){
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}