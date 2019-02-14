package com.guru.mymovies.di

import com.guru.mymovies.data.repository.MoviesRepository
import com.guru.mymovies.data.repository.MoviesRepositoryImpl
import org.koin.dsl.module.module

val RepositoryModule = module {
    factory { MoviesRepositoryImpl(get(), get()) as MoviesRepository }
}
