package ai.tomorrow.movietime.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


data class MoviePage(
    val page: Int,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int,
    val results: List<MovieProperty>
)

@Parcelize
data class MovieProperty(
    val id: Long,
    @Json(name = "vote_count") val voteCount: String,
    @Json(name = "vote_average") val voteAverage: Double,
    val title: String,
    val popularity: Double,
    @Json(name = "poster_path") val _posterPath: String,
    @Json(name = "backdrop_path") val _backdropPath: String,
    @Json(name = "original_language") val originalLanguage: String,
    val overview: String,
    @Json(name = "release_date") val releaseDate: String
) : Parcelable {
    val posterPath: String
        get() = _posterPath.substring(1)

    val backdropPath: String
        get() = _backdropPath.substring(1)
}


data class Video(
    @Json(name = "id") val videoId: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
)

data class VideoResult(
    @Json(name = "id") val movieId: Long,
    val results: List<Video>
)

