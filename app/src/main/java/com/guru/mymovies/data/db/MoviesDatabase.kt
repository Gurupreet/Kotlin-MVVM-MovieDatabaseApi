package com.guru.mymovies.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.guru.mymovies.data.db.dao.MoviesDao
import com.guru.mymovies.data.db.model.Movie
import com.guru.mymovies.util.ListTypeConverter

@Database(
    entities = [Movie::class],
    version = 1
)
@TypeConverters(ListTypeConverter::class)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object {
        private var instance: MoviesDatabase? = null
        private val LOCK = Any()
        fun getInstance(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context.applicationContext).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java, "movies.db"
            ).build()
    }
}