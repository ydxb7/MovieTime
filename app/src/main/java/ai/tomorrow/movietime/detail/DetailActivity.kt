package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.BuildConfig
import ai.tomorrow.movietime.BuildConfig.Youtube_ApiKey
import ai.tomorrow.movietime.R
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

val Youtube_ApiKey = BuildConfig.Youtube_ApiKey

class DetailActivity : YouTubeFailureRecoveryActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val youTubeView = findViewById(R.id.youtube_view) as YouTubePlayerView
        youTubeView.initialize(Youtube_ApiKey, this)

    }


    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider, player: YouTubePlayer,
        wasRestored: Boolean
    ) {
        if (!wasRestored) {
            player.cueVideo("wKJ9KzGQq0w")
        }
    }

    override val youTubePlayerProvider: YouTubePlayer.Provider
        get() = findViewById(R.id.youtube_view) as YouTubePlayerView

}

abstract class YouTubeFailureRecoveryActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    protected abstract val youTubePlayerProvider: YouTubePlayer.Provider

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider,
        errorReason: YouTubeInitializationResult
    ) {
        if (errorReason.isUserRecoverableError) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage = String.format("Video load error", errorReason.toString())
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            youTubePlayerProvider.initialize(Youtube_ApiKey, this)
        }
    }

    companion object {

        private val RECOVERY_DIALOG_REQUEST = 1
    }

}