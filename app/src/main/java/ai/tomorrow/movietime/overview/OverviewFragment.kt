package ai.tomorrow.movietime.overview

import ai.tomorrow.movietime.R
import ai.tomorrow.movietime.databinding.FragmentOverviewBinding
import ai.tomorrow.movietime.network.movieSortList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip

/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class OverviewFragment : Fragment() {

    /**
     * Lazily initialize our [OverviewViewModel].
     */
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProviders.of(this).get(OverviewViewModel::class.java)
    }

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentOverviewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the photosGrid RecyclerView with clickHandler lambda that
        // tells the viewModel when our property is clicked
        binding.postersGrid.adapter = MoviePosterGridAdapter(MoviePosterGridAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedMovie.observe(this, Observer {
            if ( null != it ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayPropertyDetailsComplete()
            }
        })

        bindChipGroup(binding)





        return binding.root
    }

    private fun bindChipGroup(binding: FragmentOverviewBinding) {
        val chipGroup = binding.moviesList
        val inflator = LayoutInflater.from(chipGroup.context)

        // Use the map() function to create a Chip for each item in regionList and return the results as a new list called children.
        val children = movieSortList.map { regionName ->
            val chip = inflator.inflate(R.layout.movie_sort, chipGroup, false) as Chip
            chip.text = regionName
            chip.tag = regionName
            chip.setOnCheckedChangeListener { button, isChecked ->
                viewModel.onSortChanged(button.tag as String, isChecked)
            }
            chip
        }

        for (chip in children) {
            chipGroup.addView(chip)
        }

        chipGroup.check(chipGroup[0].id)
    }


}