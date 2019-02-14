package com.guru.mymovies.di

import com.guru.mymovies.data.db.MoviesDatabase
import org.koin.dsl.module.module

val  DatabaseModule = module {
    single { MoviesDatabase.getInstance(get()) }
    single { get<MoviesDatabase>().moviesDao() }
}
