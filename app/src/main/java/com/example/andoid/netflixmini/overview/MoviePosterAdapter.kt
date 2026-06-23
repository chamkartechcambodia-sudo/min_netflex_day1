package com.example.andoid.netflixmini.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.andoid.netflixmini.R
import com.example.andoid.netflixmini.databinding.GridViewItemBinding
import com.example.andoid.netflixmini.network.Movie

class MoviePosterAdapter(
    private val onClick: (Movie) -> Unit
) :
    ListAdapter<Movie, MoviePosterAdapter.MovieViewHolder>(DiffCallback) {

    // Holds the views for ONE grid cell (View Binding).
    class MovieViewHolder(private val binding: GridViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.moviePoster.contentDescription = movie.title
            val posterPath = movie.posterPath
            if (posterPath != null) {
                // Build the full TMDB image URL and load it with Glide
                val fullUrl = "https://image.tmdb.org/t/p/w342$posterPath"
                Glide.with(binding.moviePoster.context)
                    .load(fullUrl)
                    .error(R.drawable.ic_connection_error)   // shown if this poster fails to load
                    .into(binding.moviePoster)
            } else {
                binding.moviePoster.setImageDrawable(null)
            }
        }
    }

    // Lets ListAdapter detect which items changed.
    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            GridViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
        holder.itemView.setOnClickListener { onClick(movie) }
    }
}