package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.BuildConfig
import ai.tomorrow.movietime.R
import ai.tomorrow.movietime.databinding.FragmentDetailBinding
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.youtube.player.YouTubePlayerFragment
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment() {
    companion object{
        val Youtube_ApiKey = BuildConfig.Youtube_ApiKey
    }

    private val youTubePlayerFragment by lazy { (context as AppCompatActivity).fragmentManager
        .findFragmentById(ai.tomorrow.movietime.R.id.youtube_fragment) as YouTubePlayerFragment }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        val movieProperty = DetailFragmentArgs.fromBundle(arguments!!).selectedMovie
        val viewModelFactory = DetailViewModelFactory(movieProperty, application)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel

//        viewModel.getVideoResults()
        youTubePlayerFragment.setRetainInstance(true)

        viewModel.hasFinishGetResults.observe(this, Observer {

//            Log.i("DetailFragment", " viewModel.videos.size = " + viewModel.videos.size)
//            Log.i("DetailFragment", " hasFinishGetResults = " + it)
            if (it){
                if (viewModel.videos.size > 0){
//                    Log.i("DetailFragment", " youTubePlayerFragment.initialize  aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" )
                    youTubePlayerFragment.initialize(Youtube_ApiKey, viewModel.onInitializedListener)
//                    viewModel.finishGetResult()
                }
                else{
                    youTubePlayerFragment.view.visibility = View.GONE
                    moive_poster.visibility = View.VISIBLE
                }
//                viewModel.finishGetResult()
            }

        })

        return binding.root
    }

    override fun onDestroy() {

        if (youTubePlayerFragment != null) {
            (context as AppCompatActivity).fragmentManager.beginTransaction().remove(youTubePlayerFragment).commitAllowingStateLoss()
        }
        super.onDestroy()
//        youTubePlayer = null
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
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

