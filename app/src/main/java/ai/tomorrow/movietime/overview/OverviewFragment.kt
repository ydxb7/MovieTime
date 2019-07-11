package ai.tomorrow.movietime.overview

import ai.tomorrow.movietime.R
import ai.tomorrow.movietime.databinding.FragmentOverviewBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

//        // TODO (04) Create an observer on viewModel.regionList. Override the required
//        // onChanged() method to include the following changes.
//        viewModel.movieList.observe(viewLifecycleOwner, object: Observer<List<String>> {
//            override fun onChanged(data: List<String>?) {
//                data ?: return
//                // TODO (05) Create a new layoutInflator from the ChipGroup.
//                val chipGroup = binding.moviesList
//                val inflator = LayoutInflater.from(chipGroup.context)
//
//                // TODO (06) Use the map() function to create a Chip for each item in regionList and
//                // return the results as a new list called children.
//                val children = data.map { regionName ->
//                    val chip = inflator.inflate(R.layout.movie_sort, chipGroup, false) as Chip
//                    chip.text = regionName
//                    chip.tag = regionName
//                    chip.setOnCheckedChangeListener { button, isChecked ->
//                        viewModel.onFilterChanged(button.tag as String, isChecked)
//                    }
//                    chip
//                }
//
//                // TODO (07) Call chipGroup.removeAllViews() to remove any views already in chipGroup.
//                chipGroup.removeAllViews()
//
//                // TODO (08)  Iterate through the list of children and add each chip to chipGroup.
//                for (chip in children) {
//                    chipGroup.addView(chip)
//                }
//            }
//        })

        return binding.root
    }


}