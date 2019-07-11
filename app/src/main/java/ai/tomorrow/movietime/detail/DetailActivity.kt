package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.BuildConfig
import ai.tomorrow.movietime.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.navArgs
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import kotlinx.android.synthetic.main.activity_detail.*


val Youtube_ApiKey = BuildConfig.Youtube_ApiKey

class DetailActivity : AppCompatActivity() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

//        val binding = ActivityDetailBinding.inflate(layoutInflater)

//        binding.setLifecycleOwner(this)
        Log.i("DetailActivity", " aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")

//        val mainActivityArgs by navArgs<DetailActivityArgs>()
//
//        val movieProperty = mainActivityArgs.selectedMovie
//
//
//        val viewModelFactory = DetailViewModelFactory(movieProperty, application)
//        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
//        binding.viewModel = viewModel


        val youTubePlayerFragment = fragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerFragment
        youTubePlayerFragment.initialize(Youtube_ApiKey, onInitializedListener)

//        youTubePlayerFragment.initialize(Youtube_ApiKey, this)
//        val youTubePlayerFragment = fragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerFragment
//
//        if (youTubePlayerFragment == null){
//            Log.i("DetailActivity", "youTubePlayerFragment == null  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
//        } else{
//            Log.i("DetailActivity", "youTubePlayerFragment find  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
//        }
//
//        youTubePlayerFragment.initialize(Youtube_ApiKey, this)

    }





}

