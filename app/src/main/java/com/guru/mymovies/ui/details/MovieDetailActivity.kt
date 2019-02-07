package com.guru.mymovies.ui.details

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.guru.mymovies.R
import com.guru.mymovies.data.db.Movie
import com.guru.mymovies.util.Constants
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.text.NumberFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class MovieDetailActivity : AppCompatActivity(), CoroutineScope {
    companion object {
       val currency =  NumberFormat.getCurrencyInstance(Locale("en", "US"))
    }
    private val movieDetailViewModel: MovieDetailViewModel by inject()
    private lateinit var movieId: String
    private var addedTime: Long = 0
    private lateinit var similarMovies: MutableList<Movie>
    private lateinit var similarMoviesAdapter: SimilarMoviesAdapter
    private lateinit var job: Job
    private lateinit var observer: Observer<List<Movie>?>
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        job = Job()
        getIntentInfo()
        setUpUI()
    }

    private fun getIntentInfo() {
        movieId = intent!!.extras["id"].toString()
        addedTime = (intent!!.extras["addedTime"]  ?: System.nanoTime() ) as Long
        toolbar.title = ""
        Glide.with(this)
            .load(Constants.movieImagePrefix+"w500/"+intent!!.extras["poster"]?.toString())
            .into(image_viewpager_experience)
        fab_scrolling.setOnClickListener { showAddedSnackBar() }
        loadMovieDetails()
        listenForErrors()
    }

    private fun setUpUI() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        similarMovies = mutableListOf()
        similarMoviesAdapter = SimilarMoviesAdapter(this, similarMovies)
        similar_movies_recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        similar_movies_recyclerview.adapter = similarMoviesAdapter
    }


    private fun showAddedSnackBar() {
        Snackbar.make(main_wrapper, "Added to your Watchlist", Snackbar.LENGTH_LONG).setAction("Undo", View.OnClickListener {  }).show()
    }

    private fun loadMovieDetails() {
        movieDetailViewModel.setCurrentMovieData(movieId, addedTime)
            launch {
                movieDetailViewModel.getMovieDetail(movieId).observe(this@MovieDetailActivity, Observer {
                    bindMovieToUi(it)
                })

                val movies = movieDetailViewModel.similarMovies.await()
                observer =  Observer { t ->
                    similarMovies.clear()
                    t?.forEach {movie ->
                        if (movie.id.toString() != movieId) {
                            similarMovies.add(movie)
                        }
                    }
                    similarMoviesAdapter.notifyDataSetChanged()
                    movies?.removeObserver(observer)
                }
                movies?.observe(this@MovieDetailActivity, observer)
        }
    }

    private fun bindMovieToUi(it: Movie?) {
        movie_title.text = it?.title
        overview.text = it?.overview
        release.text = getString(R.string.release_date) + " " +it?.releaseDate
        if (it?.voteCount != 0) {
            voting.text = getString(R.string.user_rating) + " " + it?.voteAverage + " " + "(" + it?.voteCount +
                    " votes)"
        } else {
            voting.text = "No user rating"
        }
        if (!it?.runtime?.toString().isNullOrEmpty()) {
            runtime.text = it?.runtime?.toString()+" mins"
        } else {
            runtime.text = "Not available"
        }
        if (it?.budget != null) {
            budget.text = currency.format(it?.budget)
        } else {
            budget.text = "Not available"
        }
        if (it?.revenue != null) {
            revenue.text = currency.format(it?.revenue)
        } else {
            revenue.text = "Not available"
        }
    }

    private fun listenForErrors() {
       val observer =  Observer<String> {
            Snackbar.make(main_wrapper, it!!, Snackbar.LENGTH_LONG).show()
        }
        movieDetailViewModel.error.observe(this, observer)
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
