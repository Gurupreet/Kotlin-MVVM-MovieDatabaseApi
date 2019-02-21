package com.guru.mymovies.data.api.responses

import com.google.gson.annotations.SerializedName
import com.guru.mymovies.data.db.model.Movie

data class MovieListResponse(
    @SerializedName("pages") val pages: Int,
    @SerializedName("results") val movies: List<Movie>)