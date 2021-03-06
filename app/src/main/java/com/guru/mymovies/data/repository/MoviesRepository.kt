package com.guru.mymovies.data.repository

import android.arch.lifecycle.LiveData
import com.guru.mymovies.data.db.model.Movie

interface MoviesRepository {

    suspend fun getShowingMovies(): LiveData<List<Movie>>
    suspend fun getMovieDetail(movieId: String): LiveData<Movie>
    suspend fun getSimilarMovies(movieId: String): LiveData<List<Movie>>

    fun fetchFullMovieDetails(movieId: String, addedTime: Long)
    fun fetchMoreShowingMoviesFromDataSource(page: Int)

    val error: LiveData<String>
}