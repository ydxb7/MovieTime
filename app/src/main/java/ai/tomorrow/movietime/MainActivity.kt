package ai.tomorrow.movietime

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    /**
     * Our MainActivity is only responsible for setting the content view that contains the
     * Navigation Host.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movieDb_ApiKey = BuildConfig.MovieDb_ApiKey;
        Log.i("MainActivity", movieDb_ApiKey)

    }
}
