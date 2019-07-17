package ai.tomorrow.movietime.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// Define the Domain Movie class
@Parcelize
data class Movie(
    val id: Long,
    val voteCount: String?,
    val voteAverage: Double?,
    val title: String,
    val popularity: Double?,
    val posterPath: String?,
    val backdropPath: String?,
    val originalLanguage: String?,
    val overview: String?,
    val releaseDate: String?,
    var videoId: String? = null,
    var videoKey: String? = null,
    var videoName: String? = null,
    var videoSite: String? = null,
    var videoSize: Int? = null,
    var videoType: String? = null,
    var hasVideo: Boolean = false,
    var typePopular: Boolean = false,
    var typeRate: Boolean = false,
    var typeUpcoming: Boolean = false,
    var typeNow: Boolean = false
) : Parcelable

// insert the video information get online, into the Domain Movie
fun Movie.insertNetworkVideo(videoNetwork: VideoNetwork) {
    videoId = videoNetwork.videoId
    videoKey = videoNetwork.key
    videoName = videoNetwork.name
    videoSite = videoNetwork.site
    videoSize = videoNetwork.size
    videoType = videoNetwork.type
}


// Define extension function MoviePage.asDatabaseModel(), extract the movieList and transform them into Domain Movie model.
fun MoviePage.asDomainModel(): List<Movie> {
    return results.map {
        Movie(
            id = it.id,
            voteCount = it.voteCount,
            voteAverage = it.voteAverage,
            title = it.title,
            popularity = it.popularity,
            posterPath = it.posterPath,
            backdropPath = it.backdropPath,
            originalLanguage = it.originalLanguage,
            overview = it.overview,
            releaseDate = it.releaseDate
        )
    }
}

// the data structure we get for MovieApi
data class MoviePage(
    val page: Int,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int,
    val results: List<MovieNetwork>
)

// the Network movie we get inside of the MoviePage.results
@Parcelize
data class MovieNetwork(
    val id: Long,
    @Json(name = "vote_count") val voteCount: String,
    @Json(name = "vote_average") val voteAverage: Double,
    val title: String,
    val popularity: Double,
    @Json(name = "poster_path") val _posterPath: String?,
    @Json(name = "backdrop_path") val _backdropPath: String?,
    @Json(name = "original_language") val originalLanguage: String,
    val overview: String,
    @Json(name = "release_date") val releaseDate: String
) : Parcelable {
    val posterPath: String?
        get() = _posterPath?.substring(1) ?: null

    val backdropPath: String?
        get() = _backdropPath?.substring(1) ?: null
}

// the Network video we get inside of the VideoResult.results
data class VideoNetwork(
    @Json(name = "id") val videoId: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: Int?,
    val type: String?
)

// the video json class we get online
data class VideoResult(
    @Json(name = "id") val movieId: Long,
    val results: List<VideoNetwork>
)

// the Genre of a movie
data class Genre(
    @Json(name = "id") val genreId: Int,
    @Json(name = "name") val genreName: String
)

// the Network Genre information
data class GenresNetwork(
    val genres: List<Genre>,
    val runtime: Int
)