package ai.tomorrow.movietime.repository

import ai.tomorrow.movietime.database.MoviesDatabase
import ai.tomorrow.movietime.database.asDatabaseModel
import ai.tomorrow.movietime.database.asDomainModel
import ai.tomorrow.movietime.network.*
import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// MoviesRepository class that takes a VideosDatabase argument.
// Repository for fetching decbyte videos from the network and storing them on disk.
class MoviesRepository(private val database: MoviesDatabase) {

//    val date_now = LocalDate.now()
//    val date_plus20 = date_now.plusDays(20)
//    val date_plus1 = date_now.plusDays(1)
//    val date_minus20 = date_now.minusDays(20)
//    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//
//    val date_now_milli = dateToMilli(date_now)
//    val date_plus20_milli = dateToMilli(date_plus20)
//    val date_plus1_milli = dateToMilli(date_plus1)
//    val date_minus20_milli = dateToMilli(date_minus20)

    // Transformations.map  to convert the DatabaseVideo list to a list of Video.
    // * is used to pass an array to a vararg parameter
    /**
     * A playlist of videos that can be shown on the screen.
     */
    val movies_popular: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies_popularity()) {
            it.asDomainModel()
        }

    val movies_rate: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies_rate()) {
            it.asDomainModel()
        }

    val movies_now: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies_now()) {
            it.asDomainModel()
        }

    val movies_coming: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies_coming()) {
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
            val movieList_withImage = movieList.filter { it.backdropPath != null }
            database.movieDao.insertAll(*movieList_withImage.asDatabaseModel())
        }

    }
}