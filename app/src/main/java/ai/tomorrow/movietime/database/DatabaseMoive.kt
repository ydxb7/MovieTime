package ai.tomorrow.movietime.database

import ai.tomorrow.movietime.network.Movie
import androidx.lifecycle.Transformations.map
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.load.engine.Resource


// Create the DatabaseEntities class, adding annotations for the class and the primary key.
@Entity(primaryKeys = arrayOf("id", "typePopular", "typeRate", "typeUpcoming", "typeNow"))
data class DatabaseMoive constructor(
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
    var videoType: String?,
    var hasVideo: Boolean,
    var typePopular: Boolean = false,
    var typeRate: Boolean = false,
    var typeUpcoming: Boolean = false,
    var typeNow: Boolean = false
)


// Define extension function List<Movie>.asDatabaseModel(), that returns a list of <DatabaseMoive>.
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
            videoType = it.videoType,
            hasVideo = it.hasVideo,
            typePopular = it.typePopular,
            typeRate = it.typeRate,
            typeUpcoming = it.typeUpcoming,
            typeNow = it.typeNow
        )
    }.toTypedArray()
}

// Define extension function List<DatabaseVideo>.asDomainModel(), that returns a list of <Movie>.
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
            videoType = it.videoType,
            hasVideo = it.hasVideo,
            typePopular = it.typePopular,
            typeRate = it.typeRate,
            typeUpcoming = it.typeUpcoming,
            typeNow = it.typeNow
        )
    }
}