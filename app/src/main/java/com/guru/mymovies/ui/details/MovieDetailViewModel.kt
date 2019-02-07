package com.guru.mymovies.ui.details

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.guru.mymovies.data.repository.MoviesRepository
import com.guru.mymovies.data.db.Movie
import com.guru.mymovies.util.lazyDeferred

class MovieDetailViewModel(private val moviesRepository: MoviesRepository): ViewModel() {
    private lateinit var movieId: String
    private var addedTime: Long = 0

    suspend fun getMovieDetail(movieId: String): LiveData<Movie> {
        moviesRepository.getMovieDetail(movieId).observeForever {
            if (it?.status == null) {
                moviesRepository.fetchFullMovieDetails(movieId, addedTime)
            }
        }
       return moviesRepository.getMovieDetail(movieId)
    }

    val error = moviesRepository.error

    fun setCurrentMovieData(id: String, addedTime: Long) {
        movieId = id
        this.addedTime = addedTime

    }

    val similarMovies by lazyDeferred { moviesRepository.getSimilarMovies(movieId = movieId)}

}