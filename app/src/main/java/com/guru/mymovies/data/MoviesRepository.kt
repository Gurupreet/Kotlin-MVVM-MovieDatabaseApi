package com.guru.mymovies.data

import android.arch.lifecycle.LiveData
import com.guru.mymovies.data.db.Movie

interface MoviesRepository {

    suspend fun getShowingMovies(): LiveData<List<Movie>>
    suspend fun getMovieDetail(movieId: String): LiveData<Movie>
    suspend fun getSimilarMovies(movieId: String): LiveData<List<Movie>>
    fun fetchFullMovieDetails(movieId: String, addedTime: Long)
    fun fetchMoreShowingMoviesFromDataSource(page: Int)
    val error: LiveData<String>
}