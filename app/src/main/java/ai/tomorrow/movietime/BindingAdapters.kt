package ai.tomorrow.movietime

import ai.tomorrow.movietime.network.Movie
import ai.tomorrow.movietime.overview.MovieApiStatus
import ai.tomorrow.movietime.overview.MovieListAdapter
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

const val movieDb_ApiKey = BuildConfig.MovieDb_ApiKey
private val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/"

/**
 * When there is no Mars property data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Movie>?) {
    val adapter = recyclerView.adapter as MovieListAdapter
    adapter.submitList(data)
}


/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, posterPath: String?) {
    if (posterPath == null) {
        imgView.setImageResource(R.drawable.ic_connection_error)
    } else {
        val imgUri = IMAGE_BASE_URL.toUri().buildUpon().scheme("https").appendPath(posterPath).build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
                    .transforms(RoundedCorners(20))
            )
            .into(imgView)
    }
}

/**
 * This binding adapter displays the [movieApiStatus] of the network request in an image view.  When
 * the request is loading, it displays a loading_animation.  If the request has an error, it
 * displays a broken image to reflect the connection error.  When the request is finished, it
 * hides the image view.
 */
@BindingAdapter("movieApiStatus")
fun bindStatus(statusImageView: ImageView, status: MovieApiStatus?) {
    when (status) {
        MovieApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MovieApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        MovieApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter(value = *arrayOf("showImage", "imagePath"), requireAll = false)
fun showImage(imageView: ImageView, hasVideo: Boolean, imagePath: String?) {
    if (hasVideo) {
        imageView.visibility = View.GONE
    } else {
        if (imagePath == null) {
            imageView.setImageResource(R.drawable.ic_connection_error)
        } else {
            imageView.visibility = View.VISIBLE
            val imgUri = IMAGE_BASE_URL.toUri().buildUpon().scheme("https").appendPath(imagePath).build()
            Glide.with(imageView.context)
                .load(imgUri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                        .transforms(RoundedCorners(20))
                )
                .into(imageView)
        }
    }
}

@BindingAdapter("showPlayIcon")
fun showPlayIcon(imageView: ImageView, hasVideo: Boolean) {
    if (hasVideo) {
        imageView.visibility = View.VISIBLE
    } else {
        imageView.visibility = View.GONE
    }
}