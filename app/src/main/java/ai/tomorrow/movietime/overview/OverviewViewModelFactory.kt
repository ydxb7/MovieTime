package ai.tomorrow.movietime.overview

import ai.tomorrow.movietime.detail.DetailViewModel
import ai.tomorrow.movietime.network.MovieProperty
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class OverviewViewModelFactory(private val sort: String) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            return OverviewViewModel(sort) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}