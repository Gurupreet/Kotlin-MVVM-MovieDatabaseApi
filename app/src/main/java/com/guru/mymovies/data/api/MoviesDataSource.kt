package com.guru.mymovies.data.api

import android.arch.lifecycle.LiveData
import com.guru.mymovies.data.db.model.Movie

interface MoviesDataSource {
    val showingMovies: LiveData<List<Movie>>
    val movieDetail: LiveData<Movie>
    val similarMovies: LiveData<List<Movie>>
    val error: LiveData<String>

    suspend fun getShowingMovies(page: Int)
    suspend fun getMovieDetail(id: String, addedTime: Long)
    suspend fun getSimilarMovies(id: String)
}