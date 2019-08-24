package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.BuildConfig
import ai.tomorrow.movietime.R
import ai.tomorrow.movietime.databinding.ActivityDetailBinding
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.youtube.player.YouTubePlayerFragment

class DetailActivity : AppCompatActivity() {
    val Youtube_ApiKey = BuildConfig.Youtube_ApiKey
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // The movie passed to this fragment
        val movie = DetailActivityArgs.fromBundle(intent.extras).selectedMovie

        // create ViewModel
        val viewModelFactory = DetailViewModelFactory(movie, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel

        Log.i("DetailActivity", "viewModel = " + viewModel)
        // Set the rating bar
        binding.ratingBar.numStars = 10
        binding.ratingBar.rating = movie.voteAverage?.toFloat() ?: 0.toFloat()
        binding.ratingBar.setIsIndicator(true)

        // set youTubePlayerFragment
        youTubePlayerFragment.retainInstance = true

        // If the movie has video, then show the youTubePlayerFragment, otherwise hide it
        if (viewModel.movie.hasVideo) {
            youTubePlayerFragment.initialize(Youtube_ApiKey, viewModel.onInitializedListener)
        } else {
            youTubePlayerFragment.view.visibility = View.GONE
        }
    }

    // get the youTubePlayerFragment
    private val youTubePlayerFragment by lazy {
        fragmentManager
            .findFragmentById(R.id.youtube_fragment) as YouTubePlayerFragment
    }

    override fun onDestroy() {
        // clean the youTubePlayerFragment
        if (youTubePlayerFragment != null) {
            fragmentManager.beginTransaction().remove(youTubePlayerFragment).commitAllowingStateLoss()
        }
        super.onDestroy()
    }
}
