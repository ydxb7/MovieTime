package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.network.MovieProperty
import ai.tomorrow.movietime.network.Video
import ai.tomorrow.movietime.network.VideoApi
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [MovieProperty].
 */
class DetailViewModel(movieProperty: MovieProperty, app: Application) : AndroidViewModel(app) {
    private val _selectedMovie = MutableLiveData<MovieProperty>()

    // The external LiveData for the selectedMovie
    val selectedMovie: LiveData<MovieProperty>
        get() = _selectedMovie

    // Initialize the _selectedMovie MutableLiveData
    init {
        _selectedMovie.value = movieProperty
    }

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var videos: List<Video> = ArrayList()

    init {
        getVideoResults()
    }

    private fun getVideoResults() {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            var getVideoResultsDeferred = VideoApi.retrofitService.getVideoResults()

            try {
                // this will run on a thread managed by Retrofit
                val videoResults = getVideoResultsDeferred.await()
                videos = videoResults.results
                Log.i("DetailViewModel", "videos size = " + videos.size)

            } catch (e: Exception) {
                videos = ArrayList()
                Log.i("DetailViewModel", "fetch video error")
            }
        }
    }




//    // The displayPropertyPrice formatted Transformation Map LiveData, which displays the sale
//    // or rental price.
//    val displayPropertyPrice = Transformations.map(selectedMovie) {
//        app.applicationContext.getString(
//            when (it.isRental) {
//                true -> R.string.display_price_monthly_rental
//                false -> R.string.display_price
//            }, it.price)
//    }

//    // The displayPropertyType formatted Transformation Map LiveData, which displays the
//    // "For Rent/Sale" String
//    val displayPropertyType = Transformations.map(selectedProperty) {
//        app.applicationContext.getString(R.string.display_type,
//            app.applicationContext.getString(
//                when(it.isRental) {
//                    true -> R.string.type_rent
//                    false -> R.string.type_sale
//                }))
//    }
}