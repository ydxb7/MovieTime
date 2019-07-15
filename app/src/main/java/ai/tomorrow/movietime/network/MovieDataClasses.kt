package ai.tomorrow.movietime.network

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

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
    var hasVideo: Boolean = false
) : Parcelable


// Create the DatabaseEntities class, adding annotations for the class and the primary key.
@Entity
data class DatabaseMoive constructor(
    @PrimaryKey
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
    var videoId: String?,
    var videoKey: String?,
    var videoName: String?,
    var videoSite: String?,
    var videoSize: Int?,
    var videoType: String?
)

// Define extension function List<DatabaseVideo>.asDomainModel(), that returns a list of <Video>.
fun List<Movie>.asDatabaseModel(): Array<DatabaseMoive> {
    return map {
        DatabaseMoive(
            id = it.id,
            voteCount = it.voteCount,
            voteAverage = it.voteAverage,
            title = it.title,
            popularity = it.popularity,
            posterPath = it.posterPath,
            backdropPath = it.backdropPath,
            originalLanguage = it.originalLanguage,
            overview = it.overview,
            releaseDate = it.releaseDate,
            videoId = it.videoId,
            videoKey = it.videoKey,
            videoName = it.videoName,
            videoSite = it.videoSite,
            videoSize = it.videoSize,
            videoType = it.videoType
        )
    }.toTypedArray()
}

// Define extension function List<DatabaseVideo>.asDomainModel(), that returns a list of <Video>.
fun List<DatabaseMoive>.asDomainModel(): List<Movie> {
    return map {
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
            releaseDate = it.releaseDate,
            videoId = it.videoId,
            videoKey = it.videoKey,
            videoName = it.videoName,
            videoSite = it.videoSite,
            videoSize = it.videoSize,
            videoType = it.videoType
            )
    }
}

fun Movie.insertNetworkVideo(videoNetwork: VideoNetwork){
    videoId = videoNetwork.videoId
    videoKey = videoNetwork.key
    videoName = videoNetwork.name
    videoSite = videoNetwork.site
    videoSize = videoNetwork.size
    videoType = videoNetwork.type
}


// Define extension function NetworkVideoHolder.asDatabaseModel(),
// that returns an array of <DatabaseVideo>.
fun MoviePage.asDomainModel(): List<Movie> {
    return results.map {
        Movie (
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


data class MoviePage(
    val page: Int,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int,
    val results: List<MovieNetwork>
)

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


data class VideoNetwork(
    @Json(name = "id") val videoId: String?,
    val key: String?,
    val name: String?,
    val site: String?,
    val size: Int?,
    val type: String?
)

data class VideoResult(
    @Json(name = "id") val movieId: Long,
    val results: List<VideoNetwork>
)

