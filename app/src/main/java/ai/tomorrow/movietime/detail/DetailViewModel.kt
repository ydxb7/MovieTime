package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.network.GenresNetwork
import ai.tomorrow.movietime.network.Movie
import ai.tomorrow.movietime.network.fetchGenre
import android.app.Application
import androidx.lifecycle.*
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.coroutines.*


/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [Movie].
 */

class DetailViewModel(val movie: Movie, app: Application) :
    AndroidViewModel(app) {
    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private val viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val backgroundScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    // the Genres information for movie passed to this fragment
    private val _genres = MutableLiveData<GenresNetwork>()

    val genres: LiveData<GenresNetwork>
        get() = _genres

    // reform the runtime from Int to String
    val runtimeString = Transformations.map(genres) { genres ->
        if (genres == null) {
            ""
        } else {
            // runtime
            val runtime = genres.runtime
            var runtimeString = ""
            if (runtime > 0) {
                val hours = runtime / 60
                val mins = runtime % 60

                if (hours > 0) {
                    runtimeString = runtimeString + hours + "hr "
                }
                runtimeString = runtimeString + mins + "min"
            }
            runtimeString
        }
    }

    // Reform the genres
    val genreString = Transformations.map(genres) { genres ->
        if (genres == null) {
            ""
        } else {
            genres.genres.joinToString(" / ") { it.genreName }
        }
    }

    init {
        // fetch Genres online
        getGenre(movie.id)
    }

    fun getGenre(movieId: Long) {
        viewModelScope.launch {
            val getResult = backgroundScope.async { fetchGenre(movieId) }
            _genres.value = getResult.await()
        }
    }

    // youtube initialize
    val onInitializedListener = object : YouTubePlayer.OnInitializedListener {
        override fun onInitializationSuccess(
            provider: YouTubePlayer.Provider,
            youTubePlayer: YouTubePlayer,
            wasRestored: Boolean
        ) {
            if (!wasRestored) {
                if (movie.hasVideo) {
                    youTubePlayer.cueVideo(movie.videoKey)
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
}