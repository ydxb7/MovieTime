package ai.tomorrow.movietime.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


// Create a @Dao interface called MovieDao.
@Dao
interface MovieDao {
    // Add SQL @Query getMovies_popularity() function that returns a List of DatabaseMoive.
    @Query(
        "select * from databaseMoive " +
                "WHERE typePopular == 1 " +
                "ORDER BY popularity DESC LIMIT 20"
    )
    fun getMovies_popularity(): LiveData<List<DatabaseMoive>>

    // Add SQL @Query getMovies_rate() function that returns a List of DatabaseMoive.
    @Query(
        "select * from databaseMoive " +
                "WHERE typeRate == 1 " +
                "ORDER BY voteAverage DESC LIMIT 20"
    )
    fun getMovies_rate(): LiveData<List<DatabaseMoive>>

    // Add SQL @Query getMovies_now() function that returns a List of DatabaseMoive.
    @Query(
        "select * from databaseMoive " +
                "WHERE typeNow == 1 "
    )
    fun getMovies_now(): LiveData<List<DatabaseMoive>>

    // Add SQL @Query getMovies_coming() function that returns a List of DatabaseMoive.
    @Query(
        "select * from databaseMoive " +
                "WHERE typeUpcoming == 1 "
    )
    fun getMovies_coming(): LiveData<List<DatabaseMoive>>


    // Add SQL @Insert insertAll() that replaces on conflict (or upsert).
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg videos: DatabaseMoive)
}


// Create an abstract MoviesDatabase class that extends RoomDatabase.
// Annotate MoviesDatabase with @Database,including entities and version.
@Database(entities = [DatabaseMoive::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    // Inside MoviesDatabase, create abstract val movieDao.
    abstract val movieDao: MovieDao
}

// Create an INSTANCE variable to store the MoviesDatabase singleton.
private lateinit var INSTANCE: MoviesDatabase

// Define a function getDatabase() that returns the MoviesDatabase INSTANCE.
fun getDatabase(context: Context): MoviesDatabase {

    // Inside getDatabase(), before returning INSTANCE, use a synchronized{} block to
    // check whether INSTANCE is initialized, and, if it isn’t, use DatabaseBuilder to create it.
    synchronized(MoviesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                "videos"
            ).build()
        }
    }
    return INSTANCE
}