package com.guru.mymovies.ui.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.guru.mymovies.data.MoviesRepository
import com.guru.mymovies.data.db.Movie
import com.guru.mymovies.util.lazyDeferred

class MovieListViewModel(private val moviesRepository: MoviesRepository): ViewModel() {

    val movies by lazyDeferred { moviesRepository.getShowingMovies() }
    val error = moviesRepository.error
    fun fetchMoreShowingMovies(page: Int) {
        moviesRepository.fetchMoreShowingMoviesFromDataSource(page)
    }
}