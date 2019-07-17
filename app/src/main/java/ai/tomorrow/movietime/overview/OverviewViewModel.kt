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

val movieDb_ApiKey = BuildConfig.MovieDb_ApiKey
//val movieSortMap = mapOf("popular" to "popularity", "top_rated" to "vote_average")
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel(val sort: String, application: Application) : ViewModel() {
//    private var sort = SortHolder()

    // The internal MutableLiveData that stores the status of the most recent request
    val status = MutableLiveData<MovieApiStatus>()

//    // The external immutable LiveData for the request status
//    val status: LiveData<MovieApiStatus>
//        get() = _status

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedMovie = MutableLiveData<Movie>()

    // The external immutable LiveData for the navigation property
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

    // Create a database variable and assign it to  getDatabase(), passing the application.
    private val database = getDatabase(application)

    // Define a moviesRepositor by calling the constructor and passing in the database.
    private val moviesRepository = MoviesRepository(database)

    // Internally, we use a MutableLiveData to handle navigation to the selected property
//    private val _movieList = MutableLiveData<List<Movie>>()

    // The external immutable LiveData for the navigation property
    var movieList: LiveData<List<Movie>>
//        get() = _movieList

    // Create an init block and launch a coroutine to call videosRepository.refreshVideos().
    init {
        // Get videos from the repository and assign it to a playlist variable.
        Log.i("OverviewViewModel", "sort = " +  sort)

        movieList = getMovieFromDatabase(sort)

        refreshDatabase(sort)
    }


    fun getMovieFromDatabase(sort: String): LiveData<List<Movie>>{
        return when(sort){
            "popular" -> moviesRepository.movies_popular
            "top rated" -> moviesRepository.movies_rate
            "upcoming" -> moviesRepository.movies_coming
            "now playing" -> moviesRepository.movies_now
            else -> throw IllegalArgumentException("sort name wrong")
        }
    }

    fun refreshDatabase(sort: String){
        viewModelScope.launch {
            // 因为这是 suspend function，所以要用launch
            moviesRepository.refreshMovies(sort)
        }
    }


    /**
     * When the property is clicked, set the [_navigateToSelectedProperty] [MutableLiveData]
     * @param marsProperty The [MarsProperty] that was clicked on.
     */
    fun displayPropertyDetails(movie: Movie) {
        _navigateToSelectedMovie.value = movie
    }

    /**
     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
     */
    fun displayPropertyDetailsComplete() {
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