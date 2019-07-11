package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.BuildConfig
import ai.tomorrow.movietime.databinding.ActivityDetailBinding
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.navArgs
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import kotlinx.android.synthetic.main.activity_detail.*


val Youtube_ApiKey = BuildConfig.Youtube_ApiKey

class DetailActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val application = requireNotNull(activity).application
        val binding = ActivityDetailBinding.inflate(layoutInflater)


//        val binding = FragmentDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        Log.i("DetailFragment", " aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")

        val mainActivityArgs by navArgs<DetailActivityArgs>()

        val movieProperty = mainActivityArgs.selectedMovie


        val viewModelFactory = DetailViewModelFactory(movieProperty, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel
//
//        val youTubePlayerFragment = youtube_fragment as YouTubePlayerFragment
//
//
        if (youtube_fragment == null){
            Log.i("DetailFragment", "youTubePlayerFragment == null  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        } else{
            Log.i("DetailFragment", "youTubePlayerFragment find  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
        }

//        youTubePlayerFragment.initialize("Youtube_ApiKey", this)
//


    }



    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer, wasRestored: Boolean) {
        if (!wasRestored) {
            youTubePlayer.cueVideo("nCgQDjiotG0");
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, youTubeInitializationResult: YouTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
//            youTubeInitializationResult.getErrorDialog(, 1).show()
        } else {
            val errorMessage = String.format(
                "There was an error initializing the YouTubePlayer (%1\$s)",
                youTubeInitializationResult.toString()
            )
//            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

}