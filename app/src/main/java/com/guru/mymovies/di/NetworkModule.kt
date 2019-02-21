package com.guru.mymovies.di

import com.guru.mymovies.data.api.*
import com.guru.mymovies.data.api.iterceptors.ConnectivityInterceptor
import com.guru.mymovies.data.api.iterceptors.ConnectivityInterceptorImpl
import org.koin.dsl.module.module

val NetworkModule = module {
    single { ConnectivityInterceptorImpl(get()) as ConnectivityInterceptor }
    single { MovieApi(get()) }
    single { MoviesDataSourceImpl(get()) as MoviesDataSource }
}