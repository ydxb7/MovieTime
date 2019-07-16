package ai.tomorrow.movietime.viewPages

import ai.tomorrow.movietime.R
import ai.tomorrow.movietime.databinding.FragmentDetailBinding
import ai.tomorrow.movietime.databinding.FragmentViewPagersBinding
import ai.tomorrow.movietime.detail.DetailFragmentArgs
import ai.tomorrow.movietime.detail.DetailViewModel
import ai.tomorrow.movietime.detail.DetailViewModelFactory
import ai.tomorrow.movietime.viewPages.SimpleFragmentPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class ViewPagersFragment: Fragment() {
//    lateinit var binding: FragmentViewPagersBinding
    lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentViewPagersBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        binding.setLifecycleOwner(this)
        val viewModelFactory = ViewPagersViewModelFactory(application)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(ViewPagersViewModel::class.java)

        viewPager = binding.viewpager

        // Create an adapter that knows which fragment should be shown on each page
        val adapter = SimpleFragmentPagerAdapter(childFragmentManager)

        // Set the adapter onto the view pager
        viewPager.adapter = adapter

        // Give the TabLayout the ViewPager
        val tabLayout = binding.slidingTabs
        tabLayout.setupWithViewPager(viewPager)

        return binding.root
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            viewPager.setCurrentItem(tab.getPosition())
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {

        }

        override fun onTabReselected(tab: TabLayout.Tab) {

        }
    }
}