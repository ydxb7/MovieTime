package ai.tomorrow.movietime.overview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class OverviewViewModelFactory(private val sort: String, val app: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            return OverviewViewModel(sort, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}