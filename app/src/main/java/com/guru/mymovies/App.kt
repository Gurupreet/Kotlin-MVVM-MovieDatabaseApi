package com.guru.mymovies

import android.app.Application
import com.guru.mymovies.di.*
import org.koin.android.ext.android.startKoin

class App: Application() {
    companion object {
        var hasFetched = false
    }
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(DatabaseModule, NetworkModule, RepositoryModule, ViewModelModule))
    }
}