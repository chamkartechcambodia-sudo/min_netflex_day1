package com.example.andoid.netflixmini.network

data class VideoResponse(
    val id: Int,
    val results: List<Video>
)