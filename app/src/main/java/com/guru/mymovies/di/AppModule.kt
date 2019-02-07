package com.guru.mymovies.di

import com.guru.mymovies.data.repository.MoviesRepository
import com.guru.mymovies.data.repository.MoviesRepositoryImpl
import com.guru.mymovies.data.api.*
import com.guru.mymovies.data.db.MoviesDatabase
import com.guru.mymovies.ui.details.MovieDetailViewModel
import com.guru.mymovies.ui.list.MovieListViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

object AppModule {

    fun getModule() : Module = applicationContext {
        single { MoviesDatabase.getInstance(get()) }
        single { get<MoviesDatabase>().moviesDao() }
        single { ConnectivityInterceptorImpl(get()) as ConnectivityInterceptor }
        single { MovieApi(get())}
        single { MoviesDataSourceImpl(get()) as MoviesDataSource }
        single { MoviesRepositoryImpl(get(), get()) as MoviesRepository }
        viewModel { MovieListViewModel(get()) }
        viewModel { MovieDetailViewModel(get()) }
    }
}