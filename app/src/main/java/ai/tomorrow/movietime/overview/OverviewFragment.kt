package ai.tomorrow.movietime.overview

import ai.tomorrow.movietime.R
import ai.tomorrow.movietime.databinding.FragmentOverviewBinding
import ai.tomorrow.movietime.viewPages.ViewPatersFragmentDirections
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager


/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
class OverviewFragment(val sort: String) : Fragment() {

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val application = requireNotNull(activity).application
        val binding = FragmentOverviewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        val viewModelFactory = OverviewViewModelFactory(sort, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(OverviewViewModel::class.java)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the photosGrid RecyclerView with clickHandler lambda that
        // tells the viewModel when our property is clicked
        binding.postersGrid.adapter = MovieListAdapter(MovieListAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })

        viewModel.movieList.observe(this, Observer {
            Log.i("OverviewFragment", "sort = " + sort)
            Log.i("OverviewFragment", "movieList = " + it)
        })

//        val dividerItemDecoration = DividerItemDecoration(binding.postersGrid.getContext(), R.drawable.divider)
////        val divider = ContextCompat.getDrawable(context!!, R.drawable.divider) as DividerItemDecoration
//        binding.postersGrid.addItemDecoration(dividerItemDecoration)

        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedMovie.observe(this, Observer {
            if ( null != it ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(ViewPatersFragmentDirections.actionViewPatersFragmentToDetailFragment(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayPropertyDetailsComplete()
            }
        })



        return binding.root
    }




}