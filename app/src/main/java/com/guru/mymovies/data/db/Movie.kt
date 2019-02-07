package com.guru.mymovies.data.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies_table")
data class Movie(
    @PrimaryKey val id: Long,
    val title: String,
    val poster_path: String?,
    val overview: String,
    @SerializedName("vote_count") var voteCount: Int,
    @SerializedName("vote_average") var voteAverage: Double,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("genre_ids") var genres: List<Int>?,
    val adult: Boolean,
    var tagline: String?,
    val budget: Double?,
    val revenue: Double?,
    val runtime: Int?,
    val homepage: String?,
    val status: String?,
    var addedTime: Long?)