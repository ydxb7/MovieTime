package ai.tomorrow.movietime.network

import ai.tomorrow.movietime.BuildConfig
import ai.tomorrow.movietime.overview.MovieApiStatus
import ai.tomorrow.movietime.overview.OverviewViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.Exception

private const val movieDb_ApiKey = BuildConfig.MovieDb_ApiKey;

private val MOVIE_LIST_URL = "https://api.themoviedb.org/"
private val VIDEO_RESULT_URL = "https://api.themoviedb.org/3/movie/"

private const val BASE_URL = "https://mars.udacity.com/"

enum class MovieApiSort(val value: String) {
    POPULAR("popular"), TOP_TATED("top_rated"),
    UPCOMING("upcoming"), NOW_PLAYING("now_playing")
}

val movieSortList = listOf<String>("popular", "top_rated")
//val movieSortList = listOf<String>( "popular", "top_rated", "upcoming", "now_playing")


/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofitMovie = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .addConverterFactory(ScalarsConverterFactory.create())
//    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(MOVIE_LIST_URL)
    .build()


/**
 * A public interface that exposes the [getProperties] method
 */
interface MovieApiService {
    /**
     * Returns a Coroutine [Deferred] [List] of [MovieNetwork] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22"
     * endpoint will be requested with the GET HTTP method
     */
    @GET("3/movie/{sort}")
    fun getMovieList(@Path("sort") sort: String, @Query("api_key") api_key: String):
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
            Call<MoviePage>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MovieApi {
    val retrofitService: MovieApiService by lazy { retrofitMovie.create(MovieApiService::class.java) }
}

interface VideoApiService {
    @GET("{movieId}/videos")
    fun getVideoResults(@Path("movieId") movieId: String, @Query("api_key") api_key: String):
            Call<VideoResult>
}

private val retrofitVideo = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(VIDEO_RESULT_URL)
    .build()

object VideoApi {
    val retrofitService: VideoApiService by lazy { retrofitVideo.create(VideoApiService::class.java) }
}


// Create a Coroutine scope using a job to be able to cancel when needed
val job = Job()

// the Coroutine runs using the Main (UI) dispatcher
val coroutineScope = CoroutineScope(job + Dispatchers.IO)

val mutex = Mutex()

suspend fun fetchVideo(movieList: List<Movie>) {
    try {
        movieList.map {
            var getVideoResultsDeferred =
                VideoApi.retrofitService.getVideoResults(it.id.toString(), BuildConfig.MovieDb_ApiKey)
            val videoResults = getVideoResultsDeferred.execute().body()
            if (videoResults != null && videoResults.results.size > 0) {
                it.insertNetworkVideo(videoResults.results[0])
                it.hasVideo = true
            }
        }
        Log.i("MovieApiService", "fetch video correct")
    } catch (e: Exception) {
        Log.i("MovieApiService", "fetch video error")
        Log.i("MovieApiService", "" + e)
    }
}

/**
 * Gets Movies property information from the Movie API Retrofit service and updates the
 * [MovieNetwork] [List] and [MovieApiStatus] [LiveData]. The Retrofit service returns a
 * coroutine Deferred, which we await to get the result of the transaction.
 */
suspend fun fetchMoviesList(sort: String): List<Movie> {

    // Get the Deferred object for our Retrofit request
    var getPropertiesDeferred =
        MovieApi.retrofitService.getMovieList(sort = sort, api_key = ai.tomorrow.movietime.overview.movieDb_ApiKey)
    Log.i("MovieApiService", "sort_by = " + sort)

    try {
        // this will run on a thread managed by Retrofit
        val pageResult = getPropertiesDeferred.execute().body()
        val movieList = pageResult?.asDomainModel() ?: ArrayList()
        Log.i("MovieApiService", "fetch movie list success!  ")

        mutex.withLock {
            fetchVideo(movieList)
        }
        return movieList

    } catch (e: Exception) {
        Log.i("MovieApiService", "fetch movie list error!   aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        Log.i("MovieApiService", "" + e)
        return ArrayList()
    }
}


fun fetchMovieOnline(sort: String): Deferred<List<Movie>> {
    val result = coroutineScope.async { fetchMoviesList(sort) }
    return result
}