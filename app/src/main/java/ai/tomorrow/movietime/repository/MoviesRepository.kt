package ai.tomorrow.movietime.repository

import ai.tomorrow.movietime.network.Movie
import ai.tomorrow.movietime.network.asDomainModel
import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
//
//// TODO (01) Create a VideosRepository class that takes a VideosDatabase argument.
//// Repository for fetching decbyte videos from the network and storing them on disk.
//class MoviesRepository(private val database: MoviesDatabase) {
//
//    // TODO (03) Define a Transformations.map  to convert the DatabaseVideo list to a list of Video.
//    // * is used to pass an array to a vararg parameter
//    /**
//     * A playlist of videos that can be shown on the screen.
//     */
//    val movies: LiveData<List<Movie>> =
//        Transformations.map(database.videoDao.getMovies()) {
//            it.asDomainModel()
//        }
//
//    // TODO (02) Define a suspend refreshVideos() function that gets data from the network and
//    // inserts it into the database.
//    /**
//     * Refresh the videos stored in the offline cache.
//     *
//     * This function uses the IO dispatcher to ensure the database insert database operation
//     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
//     * function is now safe to call from any thread including the Main thread.
//     *
//     * To actually load the videos for use, observe [videos]
//     */
//    suspend fun refreshMovies(sort: String) {
//
//
//
//
//
//
//        withContext(Dispatchers.IO) {
//            val playlist = Network.devbytes.getPlaylist().await()
//            database.videoDao.insertAll(*playlist.asDatabaseModel())
//        }
//    }
//}