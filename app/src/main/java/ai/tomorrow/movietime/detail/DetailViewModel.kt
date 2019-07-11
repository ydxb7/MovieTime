package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.BuildConfig
import ai.tomorrow.movietime.BuildConfig.MovieDb_ApiKey
import ai.tomorrow.movietime.network.MovieProperty
import ai.tomorrow.movietime.network.Video
import ai.tomorrow.movietime.network.VideoApi
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.R
import androidx.databinding.ViewDataBinding
import com.google.android.youtube.player.YouTubePlayerFragment


/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [MovieProperty].
 */

private const val movieDb_ApiKey = BuildConfig.MovieDb_ApiKey;

//, YouTubePlayer.OnInitializedListener
class DetailViewModel(movieProperty: MovieProperty, app: Application) :
    AndroidViewModel(app) {
    private val _selectedMovie = MutableLiveData<MovieProperty>()

    // The external LiveData for the selectedMovie
    val selectedMovie: LiveData<MovieProperty>
        get() = _selectedMovie

    private val _hasFinishGetResults = MutableLiveData<Boolean>()
    val hasFinishGetResults: LiveData<Boolean>
        get() = _hasFinishGetResults


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

    fun getVideoResults() {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            var getVideoResultsDeferred =
                VideoApi.retrofitService.getVideoResults(selectedMovie.value!!.id.toString(), MovieDb_ApiKey)

            try {
                // this will run on a thread managed by Retrofit
                val videoResults = getVideoResultsDeferred.await()
                videos = videoResults.results
//                Log.i("DetailViewModel", "videos size = " + videos.size)

            } catch (e: Exception) {
                videos = ArrayList()
                Log.i("DetailViewModel", "fetch video error")
            }
            _hasFinishGetResults.value = true
        }
    }


    val onInitializedListener = object : YouTubePlayer.OnInitializedListener {
        override fun onInitializationSuccess(
            provider: YouTubePlayer.Provider,
            youTubePlayer: YouTubePlayer,
            wasRestored: Boolean
        ) {
            Log.i("DetailViewModel", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa  wasRestored = " + wasRestored )
//            if (!wasRestored) {
                if (videos.size > 0) {
                    youTubePlayer.cueVideo(videos[0].key)
                } else {
                    youTubePlayer.cueVideo("V38cLTYYXNw")
                }
//            }
        }

        override fun onInitializationFailure(
            provider: YouTubePlayer.Provider,
            youTubeInitializationResult: YouTubeInitializationResult
        ) {

        }
    }

    fun finishGetResult() {
        _hasFinishGetResults.value = false
    }

    override fun onCleared() {
        super.onCleared()
        finishGetResult()
        viewModelJob.cancel()
        Log.i("DetailViewModel", "onCleared    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
    }

//
//    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer, wasRestored: Boolean) {
//        if (!wasRestored) {
//            youTubePlayer.cueVideo("nCgQDjiotG0");
//        }
//    }
//
//    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, youTubeInitializationResult: YouTubeInitializationResult) {
//        if (youTubeInitializationResult.isUserRecoverableError()) {
////            youTubeInitializationResult.getErrorDialog(, 1).show()
//        } else {
//            val errorMessage = String.format(
//                "There was an error initializing the YouTubePlayer (%1\$s)",
//                youTubeInitializationResult.toString()
//            )
////            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
//        }
//    }


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