package ai.tomorrow.movietime.viewPages

import ai.tomorrow.movietime.overview.OverviewFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

val movieSortList = listOf<String>("popular", "top rated", "upcoming", "now playing")

class SimpleFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount() = tabTitles.size

    private val tabTitles = movieSortList

    // Each fragment to show in ViewPager
    override fun getItem(position: Int): Fragment {
        return OverviewFragment.newInstance(tabTitles[position])
    }

    // Title for each ViewPager
    override fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position]
    }
}