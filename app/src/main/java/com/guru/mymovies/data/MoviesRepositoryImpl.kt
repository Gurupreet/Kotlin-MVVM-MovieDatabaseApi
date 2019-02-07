package com.guru.mymovies.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import android.util.Log
import com.guru.mymovies.data.api.MoviesDataSource
import com.guru.mymovies.data.db.Movie
import com.guru.mymovies.data.db.MoviesDao
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import com.guru.mymovies.App
import com.guru.mymovies.util.SingleLiveEvent

import kotlinx.coroutines.*





class MoviesRepositoryImpl(private val moviesDao: MoviesDao, private val moviesDataSource: MoviesDataSource) : MoviesRepository {


    private var similarMoviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    private var _error = SingleLiveEvent<String>()

    override val error: LiveData<String>
        get() = _error

    init {
        moviesDataSource.apply {
            showingMovies.observeForever {
                if (!it.isNullOrEmpty()) {App.hasFetched = true}
                saveMoviesToLocalDatabase(it!!)
            }
            movieDetail.observeForever {
                saveMovieDetailToLocalDatabase(it!!)
            }
            similarMovies.observeForever {
                similarMoviesLiveData.value = it
            }
            error.observeForever {
               _error.value = it ?: "Network Error"
            }

        }
    }

    override suspend fun getShowingMovies(): LiveData<List<Movie>> {
        return withContext(Dispatchers.IO) {
            if (!App.hasFetched) {
                fetchMoviesfromDataSource()
            }
            return@withContext moviesDao.getShowingMovies()
        }
    }

    override suspend fun getMovieDetail(movieId: String): LiveData<Movie> {
        return withContext(Dispatchers.IO) {
            return@withContext moviesDao.getMovieDetail(movieId)
        }
    }

    override fun fetchFullMovieDetails(movieId: String, addedTime: Long) {
        GlobalScope.launch {
            fetchMovieDetailfromDataSource(movieId, addedTime)
        }
    }

    override suspend fun getSimilarMovies(movieId: String): LiveData<List<Movie>> {
        return withContext(Dispatchers.IO) {
            fetchSimilarMoviesFromDataSource(movieId)
            return@withContext similarMoviesLiveData
        }
    }

    private fun saveMoviesToLocalDatabase(movies: List<Movie>) {
        if (movies.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                movies.forEach {
                    it.addedTime = System.nanoTime()
                }
                moviesDao.insertMovieList(movies)
            }
        }
    }


    private fun saveMovieDetailToLocalDatabase(movie: Movie) {
        GlobalScope.launch(Dispatchers.IO) {
            moviesDao.addMovieDetail(movie)
        }
    }

    private suspend fun fetchMoviesfromDataSource() {
        moviesDataSource.getShowingMovies( 1)
    }

    override fun fetchMoreShowingMoviesFromDataSource(page: Int) {
        GlobalScope.launch {
            moviesDataSource.getShowingMovies(page)
        }
    }

    suspend fun fetchMovieDetailfromDataSource(movieId: String, addedTime: Long) {
        moviesDataSource.getMovieDetail(movieId, addedTime)
    }

    private suspend fun fetchSimilarMoviesFromDataSource(movieId: String) {
        moviesDataSource.getSimilarMovies(movieId)
    }



}