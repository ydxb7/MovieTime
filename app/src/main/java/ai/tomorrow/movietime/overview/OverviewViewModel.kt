package ai.tomorrow.movietime.overview

import ai.tomorrow.movietime.BuildConfig
import ai.tomorrow.movietime.database.getDatabase
import ai.tomorrow.movietime.network.*
import ai.tomorrow.movietime.repository.MoviesRepository
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

enum class MovieApiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel(val sort: String, application: Application) : ViewModel() {

    // status is used to show loading indicator
    val status = MutableLiveData<MovieApiStatus>()

    // handle navigation to the selected movie
    private val _navigateToSelectedMovie = MutableLiveData<Movie>()

    val navigateToSelectedMovie: LiveData<Movie>
        get() = _navigateToSelectedMovie

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    // the Coroutine runs using the Main (UI) dispatcher
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // get database
    private val database = getDatabase(application)

    // Define a moviesRepositor by calling the constructor and passing in the database.
    private val moviesRepository = MoviesRepository(database)

    // get movieList to show
    var movieList: LiveData<List<Movie>>

    init {
        // Get movies from the repository and assign it to a movieList variable.
        movieList = getMovieFromDatabase(sort)

        // download the latest movieList online and refresh the databse
        refreshDatabase(sort)
    }

    // get the movieList in the databse
    fun getMovieFromDatabase(sort: String): LiveData<List<Movie>>{
        return when(sort){
            "popular" -> moviesRepository.movies_popular
            "top rated" -> moviesRepository.movies_rate
            "upcoming" -> moviesRepository.movies_coming
            "now playing" -> moviesRepository.movies_now
            else -> throw IllegalArgumentException("sort name wrong")
        }
    }

    // download the latest movieList online and refresh the databse
    fun refreshDatabase(sort: String){
        viewModelScope.launch {
            moviesRepository.refreshMovies(sort)
        }
    }

    /**
     * When the movie is clicked, set the [_navigateToSelectedMovie] [MutableLiveData]
     * @param movie The [Movie] that was clicked on.
     */
    fun displayMovieDetails(movie: Movie) {
        _navigateToSelectedMovie.value = movie
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedMovie is set to null
     */
    fun displayMovieDetailsComplete() {
        _navigateToSelectedMovie.value = null
    }


    /**
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}