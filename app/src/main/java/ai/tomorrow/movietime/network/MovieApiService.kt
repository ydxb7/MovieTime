package ai.tomorrow.movietime.network

import ai.tomorrow.movietime.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val movieDb_ApiKey = BuildConfig.MovieDb_ApiKey;
private val MOVIE_LIST_URL = "https://api.themoviedb.org/3/discover/"
private const val BASE_URL = "https://mars.udacity.com/"

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
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(MOVIE_LIST_URL)
    .build()


/**
 * A public interface that exposes the [getProperties] method
 */
interface MovieApiService {
    /**
     * Returns a Coroutine [Deferred] [List] of [MovieProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22"
     * endpoint will be requested with the GET HTTP method
     */
    @GET("movie?api_key=$movieDb_ApiKey&primary_release_date.gte=2014-09-15&primary_release_date.lte=2014-10-22")
    fun getProperties():
    // The Coroutine Call Adapter allows us to return a Deferred, a Job with a result
            Deferred<MoviePage>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MovieApi {
    val retrofitService: MovieApiService by lazy { retrofit.create(MovieApiService::class.java) }
}