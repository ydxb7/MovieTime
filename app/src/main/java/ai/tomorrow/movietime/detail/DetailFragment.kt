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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.android.youtube.player.internal.c
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    companion object{
        val Youtube_ApiKey = BuildConfig.Youtube_ApiKey
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        val movieProperty = DetailFragmentArgs.fromBundle(arguments!!).selectedMovie
        val viewModelFactory = DetailViewModelFactory(movieProperty, application)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel

        val youTubePlayerFragment = (context as AppCompatActivity).fragmentManager
            .findFragmentById(R.id.youtube_fragment) as YouTubePlayerFragment

        viewModel.hasVideo.observe(this, Observer {
            if (it){
                youTubePlayerFragment.initialize(Youtube_ApiKey, onInitializedListener)
            }
        })

        return binding.root
    }

    val onInitializedListener = object : YouTubePlayer.OnInitializedListener {
        override fun onInitializationSuccess(
            provider: YouTubePlayer.Provider,
            youTubePlayer: YouTubePlayer,
            b: Boolean
        ) {
//            if (videos.size > 0){
//                youTubePlayer.cueVideo(videos[0].key)
//            } else {
            youTubePlayer.cueVideo("V38cLTYYXNw")
//            }
        }

        override fun onInitializationFailure(
            provider: YouTubePlayer.Provider,
            youTubeInitializationResult: YouTubeInitializationResult
        ) {

        }
    }
}

