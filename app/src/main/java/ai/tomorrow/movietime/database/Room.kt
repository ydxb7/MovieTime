package ai.tomorrow.movietime.database

import ai.tomorrow.movietime.network.DatabaseMoive
import ai.tomorrow.movietime.network.dateToMilli
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter






// Create a @Dao interface called VideoDao.
@Dao
interface MovieDao {
    // Add SQL @Query getMovies() function that returns a List of DatabaseVideo.
    @Query("select * from databaseMoive" + " ORDER BY popularity DESC LIMIT 20")
    fun getMovies_popularity(): LiveData<List<DatabaseMoive>>

    // Add SQL @Query getMovies() function that returns a List of DatabaseVideo.
    @Query("select * from databaseMoive" + " ORDER BY voteAverage DESC LIMIT 20")
    fun getMovies_rate(): LiveData<List<DatabaseMoive>>

    // Add SQL @Query getMovies() function that returns a List of DatabaseVideo.
    @Query("select * from databaseMoive " +
            "WHERE releaseDate BETWEEN :date1 AND :date2 " +
            "ORDER BY releaseDate DESC LIMIT 20")
    fun getMovies_now(date1: Long, date2: Long): LiveData<List<DatabaseMoive>>

    @Query("select * from databaseMoive " +
            "WHERE releaseDate BETWEEN :date1 AND :date2 " +
            "ORDER BY releaseDate ASC LIMIT 20")
    fun getMovies_coming(date1: Long, date2: Long): LiveData<List<DatabaseMoive>>


    // Add SQL @Insert insertAll() that replaces on conflict (or upsert).
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg videos: DatabaseMoive)  // vararg: variable arguments. The function can
    // take an unknown number of arguments. It'll actually pass an array under the hood and this way
    // callers can pass a few videos without making a list.
}


// Create an abstract VideosDatabase class that extends RoomDatabase.
// Annotate VideosDatabase with @Database,including entities and version.
@Database(entities = [DatabaseMoive::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    // Inside VideosDatabase, create abstract val videoDao.
    abstract val movieDao: MovieDao
}

// Create an INSTANCE variable to store the VideosDatabase singleton.
private lateinit var INSTANCE: MoviesDatabase

// Define a function getDatabase() that returns the VideosDatabase INSTANCE.
fun getDatabase(context: Context): MoviesDatabase {

    // Inside getDatabase(), before returning INSTANCE, use a synchronized{} block to
    // check whether INSTANCE is initialized, and, if it isn’t, use DatabaseBuilder to create it.
    synchronized(MoviesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                MoviesDatabase::class.java,
                "videos").build()
        }
    }
    return INSTANCE
}