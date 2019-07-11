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

    private val youTubePlayerFragment by lazy { (context as AppCompatActivity).fragmentManager
        .findFragmentById(R.id.youtube_fragment) as YouTubePlayerFragment }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        val movieProperty = DetailFragmentArgs.fromBundle(arguments!!).selectedMovie
        val viewModelFactory = DetailViewModelFactory(movieProperty, application)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.getVideoResults()

//        val youTubePlayerFragment = (context as AppCompatActivity).fragmentManager
//            .findFragmentById(R.id.youtube_fragment) as YouTubePlayerFragment

        viewModel.hasFinishGetResults.observe(this, Observer {
            if (it){
                if (viewModel.videos.size > 0){
                    Log.i("DetailFragment", "youTubePlayerFragment.initialize aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                    youTubePlayerFragment.initialize(Youtube_ApiKey, viewModel.onInitializedListener)
                    viewModel.finishGetResult()

                    Log.i("DetailFragment", "finishGetResult aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                }
                else{
                    youTubePlayerFragment.view.visibility = View.GONE
                    moive_poster.visibility = View.VISIBLE
                }
                viewModel.finishGetResult()
            }

        })

        return binding.root
    }

    override fun onDestroy() {

        Log.i("DetailFragment", "onDestroy11 aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
//
//        val youTubePlayerFragment = (context as AppCompatActivity).fragmentManager
//            .findFragmentById(R.id.youtube_fragment) as YouTubePlayerFragment
//        val youTubePlayerFragment =
//            (context as AppCompatActivity).fragmentManager.findFragmentById(R.id.youtube_fragment) as YouTubePlayerSupportFragment?

        if (youTubePlayerFragment != null) {
            (context as AppCompatActivity).fragmentManager.beginTransaction().remove(youTubePlayerFragment).commitAllowingStateLoss()
        }
        super.onDestroy()
        Log.i("DetailFragment", "onDestroy22 aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
//        youTubePlayer = null
    }


}

