package ai.tomorrow.movietime.repository

import ai.tomorrow.movietime.database.MoviesDatabase
import ai.tomorrow.movietime.network.Movie
import ai.tomorrow.movietime.network.asDatabaseModel
import ai.tomorrow.movietime.network.asDomainModel
import ai.tomorrow.movietime.network.fetchMovieOnline
import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// MoviesRepository class that takes a VideosDatabase argument.
// Repository for fetching decbyte videos from the network and storing them on disk.
class MoviesRepository(private val database: MoviesDatabase, val sort: String) {

    // Transformations.map  to convert the DatabaseVideo list to a list of Video.
    // * is used to pass an array to a vararg parameter
    /**
     * A playlist of videos that can be shown on the screen.
     */
    val movies: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies(sort)) {
            it.asDomainModel()
        }

    // gets data from the network and inserts it into the database.
    /**
     * Refresh the movies stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the videos for use, observe [videos]
     */
    suspend fun refreshMovies(sort: String) {
        withContext(Dispatchers.IO) {
            val movieList = fetchMovieOnline(sort).await()
            database.movieDao.insertAll(*movieList.asDatabaseModel())
        }

    }
}