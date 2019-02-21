package com.guru.mymovies.data.api

import com.guru.mymovies.BuildConfig
import com.guru.mymovies.data.api.iterceptors.ConnectivityInterceptor
import com.guru.mymovies.data.api.responses.MovieListResponse
import com.guru.mymovies.data.db.model.Movie
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/now_playing?")
    fun getMovies(@Query("page") page: Int ): Deferred<retrofit2.Response<MovieListResponse>>

    @GET("movie/{id}?")
    fun getMovieDetail(@Path("id") movieId: String): Deferred<retrofit2.Response<Movie>>

    @GET("movie/{movieId}/similar?")
    fun getSimilarMovies(@Path("movieId") movieId: String): Deferred<retrofit2.Response<MovieListResponse>>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): MovieApi {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key",BuildConfig.API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.themoviedb.org/3/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieApi::class.java)
        }
    }
}