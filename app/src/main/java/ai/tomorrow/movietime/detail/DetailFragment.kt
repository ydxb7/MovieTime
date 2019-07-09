package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.databinding.FragmentDetailBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

/**
 * This [Fragment] shows the detailed information about a selected piece of Mars real estate.
 * It sets this information in the [DetailViewModel], which it gets as a Parcelable property
 * through Jetpack Navigation's SafeArgs.
 */
class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        val movieProperty = DetailFragmentArgs.fromBundle(arguments!!).selectedMovie
        val viewModelFactory = DetailViewModelFactory(movieProperty, application)
        binding.viewModel = ViewModelProviders.of(
            this, viewModelFactory).get(DetailViewModel::class.java)
        return binding.root
    }
}