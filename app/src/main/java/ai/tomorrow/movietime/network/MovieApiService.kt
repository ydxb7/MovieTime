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
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

// My movie DB key
const val movieDb_ApiKey = BuildConfig.MovieDb_ApiKey

private val MOVIE_LIST_URL = "https://api.themoviedb.org/"
private val VIDEO_RESULT_URL = "https://api.themoviedb.org/3/movie/"


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
 * A public interface that exposes the [getMovieList], [getMovieDetail] methods
 */
interface MovieApiService {

    // GET request to get the movie list on Web
    @GET("3/movie/{sort}")
    fun getMovieList(@Path("sort") sort: String, @Query("api_key") api_key: String):
            Call<MoviePage>

    // GET request to get the movie detail on Web
    @GET("3/movie/{movieId}")
    fun getMovieDetail(@Path("movieId") movieId: String, @Query("api_key") api_key: String):
            Call<GenresNetwork>

}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MovieApi {
    val retrofitService: MovieApiService by lazy { retrofitMovie.create(MovieApiService::class.java) }
}

/**
 * A public interface that exposes the [getVideoResults] method
 */
interface VideoApiService {
    // GET request to get the video information on Web
    @GET("{movieId}/videos")
    fun getVideoResults(@Path("movieId") movieId: String, @Query("api_key") api_key: String):
            Call<VideoResult>
}

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofitVideo = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(VIDEO_RESULT_URL)
    .build()

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object VideoApi {
    val retrofitService: VideoApiService by lazy { retrofitVideo.create(VideoApiService::class.java) }
}


// Create a Coroutine scope using a job to be able to cancel when needed
val job = Job()

// the Coroutine runs using the Main (UI) dispatcher
val coroutineScope = CoroutineScope(job + Dispatchers.IO)

val mutex = Mutex()

// fetch video information of each movie in the movieList
fun fetchVideo(movieList: List<Movie>) {
    try {
        movieList.map {
            var getVideoResultsDeferred =
                VideoApi.retrofitService.getVideoResults(it.id.toString(), BuildConfig.MovieDb_ApiKey)
            // get video information on web
            val videoResults = getVideoResultsDeferred.execute().body()
            // insert the video information into the Domain Movie object
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
suspend fun fetchMoviesList(movieType: String): List<Movie> {

    var getPropertiesDeferred = when (movieType) {
        // Get the Deferred object for our Retrofit request
        "popular" -> MovieApi.retrofitService.getMovieList(
            sort = "popular",
            api_key = movieDb_ApiKey
        )

        "top rated" -> MovieApi.retrofitService.getMovieList(
            sort = "top_rated",
            api_key = movieDb_ApiKey
        )

        "upcoming" -> MovieApi.retrofitService.getMovieList(
            sort = "upcoming",
            api_key = movieDb_ApiKey
        )

        "now playing" -> MovieApi.retrofitService.getMovieList(
            sort = "now_playing",
            api_key = movieDb_ApiKey
        )

        else -> throw IllegalArgumentException("movie type is wrong!")

    }


    try {
        // get the movies result
        val pageResult = getPropertiesDeferred.execute().body()

        // extract the movieList
        val movieList = pageResult?.asDomainModel() ?: ArrayList()

        // define the movie type for different sort
        when(movieType){
            "popular" -> movieList.map{ it.typePopular = true }
            "top rated" -> movieList.map{ it.typeRate = true }
            "upcoming" -> movieList.map{ it.typeUpcoming = true }
            "now playing" -> movieList.map{ it.typeNow = true }
        }

        Log.i("MovieApiService", "sort_by = " + movieType)
        Log.i("MovieApiService", "fetch movie list success!  ")

        mutex.withLock {
            fetchVideo(movieList)
        }
        return movieList

    } catch (e: Exception) {
        Log.i("MovieApiService", "fetch movie list error! ")
        Log.i("MovieApiService", "" + e)
        return ArrayList()
    }
}


fun fetchMovieOnline(sort: String): Deferred<List<Movie>> {
    val result = coroutineScope.async { fetchMoviesList(sort) }
    return result
}


// get the movie detail information from the movieId
fun fetchGenre(movieId: Long): GenresNetwork?{
    val getVideoResultsDeferred = MovieApi.retrofitService.getMovieDetail(movieId = movieId.toString(), api_key = BuildConfig.MovieDb_ApiKey)

    try {
        val genreResults = getVideoResultsDeferred.execute().body()
        Log.i("MovieApiService", "fetch Genre correct")
        return genreResults
    } catch (e: Exception) {
        Log.i("MovieApiService", "fetch Genre error")
        Log.i("MovieApiService", "" + e)
        return null
    }
}

fun fetcGenreOnline(movieId: Long): Deferred<GenresNetwork?> {
    val result = coroutineScope.async { fetchGenre(movieId) }
    return result
}