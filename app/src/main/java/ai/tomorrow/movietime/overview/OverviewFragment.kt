package ai.tomorrow.movietime.overview

import ai.tomorrow.movietime.databinding.FragmentOverviewBinding
import ai.tomorrow.movietime.viewPages.ViewPagersFragmentDirections
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController


/**
 * This fragment shows the movies by different sort.
 */
class OverviewFragment : Fragment() {

    companion object{
        private val SORT_TYPE = "sortType"

        fun newInstance(sort: String): OverviewFragment {
            val overviewFragment = OverviewFragment()
            val args = Bundle()
            args.putString(SORT_TYPE, sort)
            overviewFragment.setArguments(args)
            return overviewFragment
        }
    }

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

        // Get sort type
        val sort = arguments!!.getString(SORT_TYPE)

        // create ViewModel
        val viewModelFactory = OverviewViewModelFactory(sort, application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(OverviewViewModel::class.java)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the movieList RecyclerView with clickHandler lambda that
        // tells the viewModel when our movie is clicked
        binding.postersGrid.adapter = MovieListAdapter(MovieListAdapter.OnClickListener {
            viewModel.displayMovieDetails(it)
        })

        // set the loading indicator, if only when there is no movie in the movieList, we show the loading indicator
        viewModel.movieList.observe(this, Observer {
            if (it.size == 0){
                viewModel.status.value = MovieApiStatus.LOADING
            }else{
                viewModel.status.value = MovieApiStatus.DONE
            }
        })


        // Observe the navigateToSelectedMovie LiveData and Navigate when it isn't null
        // After navigating, call displayMovieDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToSelectedMovie.observe(this, Observer {
            if ( null != it ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(ViewPagersFragmentDirections.actionViewPagersFragmentToDetailActivity(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayMovieDetailsComplete()
            }
        })

        return binding.root
    }
}