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

// MoviesRepository class that takes a MoviesDatabase argument.
// Repository for fetching movie data from the network and storing them on disk.
class MoviesRepository(private val database: MoviesDatabase) {

    // get the popular movies list from the database, and transform them to the DomainModel
    val movies_popular: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies_popularity()) {
            it.asDomainModel()
        }

    // get the top rated movies list from the database, and transform them to the DomainModel
    val movies_rate: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies_rate()) {
            it.asDomainModel()
        }

    // get the now playing movies list from the database, and transform them to the DomainModel
    val movies_now: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies_now()) {
            it.asDomainModel()
        }

    // get the upcoming movies list from the database, and transform them to the DomainModel
    val movies_coming: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies_coming()) {
            it.asDomainModel()
        }

    // Refresh the movies stored in the offline cache.
    suspend fun refreshMovies(sort: String) {
        // This function uses the IO dispatcher to ensure the database insert database operation happens on the IO dispatcher.
        withContext(Dispatchers.IO) {
            // get the movieList with their video information online
            val movieList = fetchMovieOnline(sort).await()
            // remove the movie without a backdrop poster
            val movieList_withImage = movieList.filter { it.backdropPath != null }
            // insert the movieList into the database
            database.movieDao.insertAll(*movieList_withImage.asDatabaseModel())
        }

    }


}