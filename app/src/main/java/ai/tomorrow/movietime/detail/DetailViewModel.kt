package ai.tomorrow.movietime.detail

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

//, YouTubePlayer.OnInitializedListener
class DetailViewModel(movieProperty: MovieProperty, app: Application) :
    AndroidViewModel(app) {
    private val _selectedMovie = MutableLiveData<MovieProperty>()

    // The external LiveData for the selectedMovie
    val selectedMovie: LiveData<MovieProperty>
        get() = _selectedMovie

    private val _hasVideo = MutableLiveData<Boolean>()
    val hasVideo: LiveData<Boolean>
        get() = _hasVideo


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
                _hasVideo.value = true
                // this will run on a thread managed by Retrofit
                val videoResults = getVideoResultsDeferred.await()
                videos = videoResults.results
                if (videos.size > 0){
                    _hasVideo.value = true
                } else {
                    _hasVideo.value = false
                }
                Log.i("DetailViewModel", "videos size = " + videos.size)

            } catch (e: Exception) {
                videos = ArrayList()
                Log.i("DetailViewModel", "fetch video error")
            }
        }
    }


    val onInitializedListener = object : YouTubePlayer.OnInitializedListener {
        override fun onInitializationSuccess(
            provider: YouTubePlayer.Provider,
            youTubePlayer: YouTubePlayer,
            b: Boolean
        ) {
//            if (videos.size > 0){
//                youTubePlayer.cueVideo(videos[0].key)
//            } else {
                youTubePlayer.cueVideo("V38cLTYYXNw")
//            }
        }

        override fun onInitializationFailure(
            provider: YouTubePlayer.Provider,
            youTubeInitializationResult: YouTubeInitializationResult
        ) {

        }
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