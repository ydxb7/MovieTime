package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.BuildConfig
import ai.tomorrow.movietime.BuildConfig.MovieDb_ApiKey
import ai.tomorrow.movietime.network.Movie
import ai.tomorrow.movietime.network.MovieNetwork
import ai.tomorrow.movietime.network.VideoNetwork
import ai.tomorrow.movietime.network.VideoApi
import android.app.Application
import android.util.Log
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


/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [Movie].
 */

private const val movieDb_ApiKey = BuildConfig.MovieDb_ApiKey;

//, YouTubePlayer.OnInitializedListener
class DetailViewModel(movieNetwork: Movie, app: Application) :
    AndroidViewModel(app) {
    private val _selectedMovie = MutableLiveData<Movie>()

    // The external LiveData for the selectedMovie
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie

    private val _hasFinishGetResults = MutableLiveData<Boolean>()
    val hasFinishGetResults: LiveData<Boolean>
        get() = _hasFinishGetResults


    // Initialize the _selectedMovie MutableLiveData
    init {
        _selectedMovie.value = movieNetwork
    }

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var videoNetworks: List<VideoNetwork> = ArrayList()


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
                videoNetworks = videoResults.results
//                Log.i("DetailViewModel", "videoNetworks size = " + videoNetworks.size)

            } catch (e: Exception) {
                videoNetworks = ArrayList()
                Log.i("DetailViewModel", "fetch video error")
                Log.i("DetailViewModel", "" + e)

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
            if (!wasRestored) {
                if (videoNetworks.size > 0) {
                    youTubePlayer.cueVideo(videoNetworks[0].key)
                } else {
                    youTubePlayer.cueVideo("V38cLTYYXNw")
                }
            }
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
    }


}