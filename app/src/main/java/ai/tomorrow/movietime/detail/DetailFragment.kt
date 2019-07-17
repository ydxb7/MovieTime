package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.BuildConfig
import ai.tomorrow.movietime.R
import ai.tomorrow.movietime.databinding.FragmentDetailBinding
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.youtube.player.YouTubePlayerFragment

val Youtube_ApiKey = BuildConfig.Youtube_ApiKey

class DetailFragment : Fragment() {

    // get the youTubePlayerFragment
    private val youTubePlayerFragment by lazy { (context as AppCompatActivity).fragmentManager
        .findFragmentById(ai.tomorrow.movietime.R.id.youtube_fragment) as YouTubePlayerFragment }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        // The movie passed to this fragment
        val movie = DetailFragmentArgs.fromBundle(arguments!!).selectedMovie

        // create ViewModel
        val viewModelFactory = DetailViewModelFactory(movie, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel

        // Set the rating bar
        binding.ratingBar.setNumStars(10)
        binding.ratingBar.rating = movie.voteAverage?.toFloat() ?: 0.toFloat()
        binding.ratingBar.setIsIndicator(true)

        // set youTubePlayerFragment
        youTubePlayerFragment.setRetainInstance(true)

        // If the movie has video, then show the youTubePlayerFragment, otherwise hide it
        if (viewModel.movie.hasVideo){
            youTubePlayerFragment.initialize(Youtube_ApiKey, viewModel.onInitializedListener)
        } else {
            youTubePlayerFragment.view.visibility = View.GONE
        }

        return binding.root
    }

    override fun onDestroy() {
        // clean the youTubePlayerFragment
        if (youTubePlayerFragment != null) {
            (context as AppCompatActivity).fragmentManager.beginTransaction().remove(youTubePlayerFragment).commitAllowingStateLoss()
        }
        super.onDestroy()
    }

    // Set different sizes of the youTubePlayerFragment according to the orientation of the phone
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val currentOrientation = resources.configuration.orientation

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            val screenWidth = dpToPx(resources.configuration.screenWidthDp)
            val videoWidth = screenWidth - screenWidth / 4 - resources.getDimension(R.dimen.videoPadding).toInt() * 2
            setLayoutSize(youTubePlayerFragment.getView()!!, videoWidth, ViewGroup.LayoutParams.WRAP_CONTENT)

        } else {
            setLayoutSize(youTubePlayerFragment.getView()!!,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    // Set the layout size
    private fun setLayoutSize(view: View, width: Int, height: Int) {
        val params = view.layoutParams
        params.width = width
        params.height = height
        view.layoutParams = params
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density + 0.5f).toInt()
    }

}

