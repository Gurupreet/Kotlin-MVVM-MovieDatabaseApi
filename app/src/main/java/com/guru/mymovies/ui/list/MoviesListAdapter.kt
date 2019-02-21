package com.guru.mymovies.ui.list

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.guru.mymovies.R
import com.guru.mymovies.data.db.model.Movie
import com.guru.mymovies.ui.details.MovieDetailActivity
import com.guru.mymovies.util.Constants
import kotlinx.android.synthetic.main.row_movie_layout.view.*


class MoviesListAdapter(val context: Context, val list: MutableList<Movie>): RecyclerView.Adapter<MoviesListAdapter.MovieVH>() {
    private var movieList: List<Movie> = mutableListOf()

    init {
        setHasStableIds(true)
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MovieVH {
        return MovieVH(LayoutInflater.from(parent.context).inflate(R.layout.row_movie_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun getItemId(position: Int): Long {
        return movieList[position].id
    }

    fun setMovieList(newMovieList: List<Movie>) {
            movieList = newMovieList
            notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        val movie = movieList[position]
        holder.bind(movie)
    }

    class MovieVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
           itemView.title.text = movie.title
           Glide.with(itemView.context)
               .load(Constants.movieImagePrefix+"w500/"+movie.poster_path)
               .into(itemView.poster)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MovieDetailActivity::class.java)
                intent.putExtra("addedTime", movie.addedTime)
                intent.putExtra("id", movie.id)
                intent.putExtra("poster", movie.poster_path)
                itemView.context.startActivity(intent)
            }
        }
    }
}