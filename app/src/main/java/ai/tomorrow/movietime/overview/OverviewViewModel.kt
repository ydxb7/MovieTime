package ai.tomorrow.movietime.overview

import ai.tomorrow.movietime.network.MovieApi
import ai.tomorrow.movietime.network.MovieProperty
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

enum class MovieApiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {
    private var sort = SortHolder()

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<MovieApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<MovieApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of MovieProperty with new values
    private val _properties = MutableLiveData<List<MovieProperty>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val properties: LiveData<List<MovieProperty>>
        get() = _properties

    private val _movieList = MutableLiveData<List<String>>()

    val movieList: LiveData<List<String>>
        get() = _movieList

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedMovie = MutableLiveData<MovieProperty>()

    // The external immutable LiveData for the navigation property
    val navigateToSelectedMovie: LiveData<MovieProperty>
        get() = _navigateToSelectedMovie

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Call getMoviesProperties() on init so we can display status immediately.
     */
    init {
        getMoviesProperties()
    }


    /**
     * Gets Movies property information from the Movie API Retrofit service and updates the
     * [MovieProperty] [List] and [MovieApiStatus] [LiveData]. The Retrofit service returns a
     * coroutine Deferred, which we await to get the result of the transaction.
     */
    private fun getMoviesProperties() {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            var getPropertiesDeferred = MovieApi.retrofitService.getProperties()

            try {
                _status.value = MovieApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val pageResult = getPropertiesDeferred.await()
                _status.value = MovieApiStatus.DONE
                _properties.value = pageResult.results
            } catch (e: Exception) {
                _status.value = MovieApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }

//    private fun onQueryChanged() {
//        coroutineScope.launch {
//            try {
//                // this will run on a thread managed by Retrofit
//                _properties.value = repository.getChaptersForFilter(filter.currentValue)
//                repository.getFilters().let {
//                    // only update the filters list if it's changed since the last time
//                    if (it != _regionList.value) {
//                        _regionList.value = it
//                    }
//                }
//            } catch (e: IOException) {
//                _gdgList.value = listOf()
//            }
//        }
//    }

//    fun onSortChanged(filter: String, isChecked: Boolean) {
//        if (this.sort.update(filter, isChecked)) {
//            onQueryChanged()
//        }
//    }
    /**
     * When the property is clicked, set the [_navigateToSelectedProperty] [MutableLiveData]
     * @param marsProperty The [MarsProperty] that was clicked on.
     */
    fun displayPropertyDetails(movieProperty: MovieProperty) {
        _navigateToSelectedMovie.value = movieProperty
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

    private class SortHolder {
        var currentValue: String? = null
            private set

        fun update(changedSort: String, isChecked: Boolean): Boolean {
            if (isChecked) {
                currentValue = changedSort
                return true
            } else if (currentValue == changedSort) {
                currentValue = null
                return true
            }
            return false
        }
    }
}