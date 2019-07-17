package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.BuildConfig
import ai.tomorrow.movietime.BuildConfig.MovieDb_ApiKey
import ai.tomorrow.movietime.database.getDatabase
import ai.tomorrow.movietime.network.*
import ai.tomorrow.movietime.repository.MoviesRepository
import android.app.Application
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.*
import com.bumptech.glide.Glide.init
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.coroutines.*


/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [Movie].
 */

private const val movieDb_ApiKey = BuildConfig.MovieDb_ApiKey;

//, YouTubePlayer.OnInitializedListener
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

    private val _genres = MutableLiveData<GenresNetwork>()

    // The external immutable LiveData for the navigation property
    val genres: LiveData<GenresNetwork>
        get() = _genres

    val runtimeString = Transformations.map(genres) { genres ->
        if(genres == null){
            "--"
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

    val genreString = Transformations.map(genres) { genres ->
        if(genres == null){
            ""
        } else {
            genres.genres.joinToString(" / ") { it.genreName }
        }
    }

    init {
        getGenre(movie.id)
    }

    fun getGenre(movieId: Long) {
        viewModelScope.launch {
            _genres.value = fetcGenreOnline(movieId).await()
        }
    }

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