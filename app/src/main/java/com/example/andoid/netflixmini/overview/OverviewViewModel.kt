package com.example.andoid.netflixmini.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.andoid.netflixmini.BuildConfig
import com.example.andoid.netflixmini.network.Movie
import com.example.andoid.netflixmini.network.TmdbApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.collections.take

// The three states a network request can be in.
enum class TmdbApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    // Internal (changeable) vs. external (read-only) LiveData — a common pattern.
    // Status is now an enum, not a String.
    private val _status = MutableLiveData<TmdbApiStatus>()
    val status: LiveData<TmdbApiStatus> get() = _status

    // The real list of movies we got from TMDB.
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> get() = _popularMovies

    private val _topRatedMovies = MutableLiveData<List<Movie>>()
    val topRatedMovies: LiveData<List<Movie>> get() = _topRatedMovies

    private val _nowPlayingMovies = MutableLiveData<List<Movie>>()
    val nowPlayingMovies: LiveData<List<Movie>> get() = _nowPlayingMovies

    private val _upcomingMovies = MutableLiveData<List<Movie>>()
    val upcomingMovies: LiveData<List<Movie>> get() = _upcomingMovies

    private val _featured = MutableLiveData<Movie>()
    val featured: LiveData<Movie> get() = _featured


    private fun loadHome(){
        viewModelScope.launch {
            _status.value = TmdbApiStatus.LOADING
            try {
                val key = BuildConfig.TMDB_API_KEY

                coroutineScope {
                    val popularDeferred = async { TmdbApi.retrofitService.getPopular(key) }
                    val topRatedDeferred = async { TmdbApi.retrofitService.getTopRated(key) }
                    val nowPlayingDeferred = async { TmdbApi.retrofitService.getNowPlaying(key) }
                    val upcomingDeferred = async { TmdbApi.retrofitService.getUpcoming(key) }

                    val popular = popularDeferred.await().results
                    val topRated = topRatedDeferred.await().results
                    val nowPlaying = nowPlayingDeferred.await().results
                    val upcoming = upcomingDeferred.await().results

                    _popularMovies.value = popular
                    _topRatedMovies.value = topRated
                    _nowPlayingMovies.value = nowPlaying
                    _upcomingMovies.value = upcoming
                    _featured.value = popular.firstOrNull()
                }

                _status.value = TmdbApiStatus.DONE

            } catch (e: Exception) {
                _popularMovies.value = listOf()
                _topRatedMovies.value = listOf()
                _nowPlayingMovies.value = listOf()
                _upcomingMovies.value = listOf()
                _status.value = TmdbApiStatus.ERROR
            }
        }
    }


    // A friendly message shown while loading or on error (empty when DONE).
    val statusMessage: LiveData<String> = status.map { state ->
        when (state) {
            TmdbApiStatus.LOADING -> "Loading movies…"
            TmdbApiStatus.ERROR -> "⚠ Couldn't load movies.\nCheck your Internet connection."
            else -> ""
        }
    }

    // The first 10 titles as plain text — just to prove we have real data.
    val moviesText: LiveData<String> = movies.map { list ->
        list.take(10).joinToString("\n") { it.title }
    }

    // Fetch immediately when the ViewModel is created.
    init {
        loadHome()
    }

   /* private fun getPopularMovies() {
        viewModelScope.launch {
            _status.value = TmdbApiStatus.LOADING
            try {
                val response = TmdbApi.retrofitService.getPopular(BuildConfig.TMDB_API_KEY)
                _movies.value = response.results
                _status.value = TmdbApiStatus.DONE
            } catch (e: Exception) {
                _movies.value = listOf()
                _status.value = TmdbApiStatus.ERROR
            }
        }
    }*/
}