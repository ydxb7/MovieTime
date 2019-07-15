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
class DetailViewModel(val movie: Movie, app: Application) :
    AndroidViewModel(app) {

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