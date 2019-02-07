package com.guru.mymovies.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovieList(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovieDetail(movie: Movie)

    @Transaction
    @Query("select * from movies_table order by addedTime")
    fun getShowingMovies(): LiveData<List<Movie>>

    @Query("DELETE FROM movies_table")
    fun deleteAll()

    @Query("select * from movies_table where id =:movieId")
    fun getMovieDetail(movieId: String): LiveData<Movie>
}