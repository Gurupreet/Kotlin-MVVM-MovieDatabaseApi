package com.guru.mymovies.data.api

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.guru.mymovies.data.db.model.Movie
import java.lang.Exception

class MoviesDataSourceImpl(private val movieApi: MovieApi) : MoviesDataSource {

    private val _similarMovies= MutableLiveData<List<Movie>>()
    private val _movieDetail = MutableLiveData<Movie>()
    private val _showingMovies = MutableLiveData<List<Movie>>()
    private val _error = MutableLiveData<String>()

    override val showingMovies: LiveData<List<Movie>>
        get() = _showingMovies

    override val movieDetail: LiveData<Movie>
        get() =_movieDetail

    override val similarMovies: LiveData<List<Movie>>
        get() = _similarMovies

    override val error: LiveData<String>
        get() = _error

    override suspend fun getMovieDetail(id: String, addedTime: Long) {
        try {
            var result = movieApi.getMovieDetail(id).await()
            if (result.isSuccessful) {
                result.body()?.addedTime = addedTime
                _movieDetail.postValue(result?.body())
            } else {
                _error.postValue("Failed to load movie details")
            }
        } catch (e: Exception) {
            _error.postValue("No internet connection")
        }
    }

    override suspend fun getShowingMovies(page: Int) {
        try {
            var result = movieApi.getMovies(page).await()
            if (result.isSuccessful && !result.body()?.movies.isNullOrEmpty()) {
                _showingMovies.postValue(result.body()?.movies)
            } else {
                _error.postValue("No Movies Found")
            }
        } catch (e: Exception) {
            _error.postValue("No internet connection")
        }
    }

    override suspend fun getSimilarMovies(id: String) {
        try {
            var result = movieApi.getSimilarMovies(id).await()
            if (result.isSuccessful) {
                if (result.body()?.movies.isNullOrEmpty()) {
                    _error.postValue("No similar movies found")
                    _similarMovies.postValue(null)
                } else {
                    _similarMovies.postValue(result.body()?.movies)
                }
            } else {
                _error.postValue("No similar movies found")
            }
        } catch (e: Exception) {
            _error.postValue("No internet connection")
        }
    }


}