package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.BuildConfig
import ai.tomorrow.movietime.BuildConfig.Youtube_ApiKey
import ai.tomorrow.movietime.R
import ai.tomorrow.movietime.databinding.FragmentDetailBinding
import android.app.AppComponentFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.android.youtube.player.internal.c
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    val Youtube_ApiKey = BuildConfig.Youtube_ApiKey

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        val movieProperty = DetailFragmentArgs.fromBundle(arguments!!).selectedMovie
        val viewModelFactory = DetailViewModelFactory(movieProperty, application)
        binding.viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(DetailViewModel::class.java)

        val youTubePlayerFragment = (context as AppCompatActivity).fragmentManager.findFragmentById(R.id.youtube_fragment)  as YouTubePlayerFragment

        youTubePlayerFragment.initialize(Youtube_ApiKey, onInitializedListener)
        return binding.root
    }

    private val onInitializedListener = object : YouTubePlayer.OnInitializedListener {
        override fun onInitializationSuccess(
            provider: YouTubePlayer.Provider,
            youTubePlayer: YouTubePlayer,
            b: Boolean
        ) {
            youTubePlayer.cueVideo("nCgQDjiotG0")

        }

        override fun onInitializationFailure(
            provider: YouTubePlayer.Provider,
            youTubeInitializationResult: YouTubeInitializationResult
        ) {

        }
    }
}