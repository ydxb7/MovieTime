package ai.tomorrow.movietime.viewPages

import ai.tomorrow.movietime.network.movieSortList
import ai.tomorrow.movietime.overview.OverviewFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SimpleFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount() = tabTitles.size

    private val tabTitles = movieSortList // tab内容

    override fun getItem(position: Int): Fragment {
//        return if (position == 0) {
           return OverviewFragment(tabTitles[position])
//        } else if (position == 1) {
//            FamilyFragment()
//        } else if (position == 2) {
//            ColorsFragment()
//        } else {
//            PhrasesFragment()
//        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position] // 加上tab
    }

}