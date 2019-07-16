package ai.tomorrow.movietime.viewPages

import ai.tomorrow.movietime.database.getDatabase
import ai.tomorrow.movietime.detail.DetailViewModel
import ai.tomorrow.movietime.network.Movie
import ai.tomorrow.movietime.network.movieSortList
import ai.tomorrow.movietime.repository.MoviesRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

//, YouTubePlayer.OnInitializedListener
class ViewPagersViewModel(application: Application) : AndroidViewModel(application) {

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

    init {
        viewModelScope.launch {
            for (sort in movieSortList){
                moviesRepository.refreshMovies(sort)
            }
        }
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

class ViewPagersViewModelFactory(
    private val application: Application
)
    : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewPagersViewModel::class.java)) {
            return ViewPagersViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
