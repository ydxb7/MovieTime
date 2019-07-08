package ai.tomorrow.movietime.network

import com.squareup.moshi.Json

data class MovieProperty(
    val id: Long,
    @Json(name = "vote_count") val voteCount: String,
    @Json(name = "vote_average") val voteAverage: Double,
    val title: String,
    val popularity: Double,
    @Json(name = "poster_path") val posterPath: String,
    @Json(name = "original_language") val originalLanguage: String,
    val overview: String,
    @Json(name = "release_date") val releaseDate: String
)