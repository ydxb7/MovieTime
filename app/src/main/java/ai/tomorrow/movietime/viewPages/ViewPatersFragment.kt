package ai.tomorrow.movietime.viewPages

import ai.tomorrow.movietime.databinding.FragmentViewPagersBinding
import ai.tomorrow.movietime.viewPages.SimpleFragmentPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ViewPatersFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentViewPagersBinding.inflate(inflater)
        val viewPager = binding.viewpager

        // Create an adapter that knows which fragment should be shown on each page
        val adapter = SimpleFragmentPagerAdapter(childFragmentManager)

        // Set the adapter onto the view pager
        viewPager.adapter = adapter

        // Give the TabLayout the ViewPager
        val tabLayout = binding.slidingTabs
        tabLayout.setupWithViewPager(viewPager)



        return binding.root
    }

}