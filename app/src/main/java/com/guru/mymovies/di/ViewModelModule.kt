package com.guru.mymovies.di

import com.guru.mymovies.ui.details.MovieDetailViewModel
import com.guru.mymovies.ui.list.MovieListViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val ViewModelModule = module {
    viewModel { MovieListViewModel(get()) }
    viewModel { MovieDetailViewModel(get()) }
}