package ai.tomorrow.movietime.detail

import ai.tomorrow.movietime.network.MovieProperty
import android.app.Application
import androidx.lifecycle.*


/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [MovieProperty].
 */
class DetailViewModel(movieProperty: MovieProperty, app: Application) : AndroidViewModel(app) {
    private val _selectedMovie = MutableLiveData<MovieProperty>()

    // The external LiveData for the selectedMovie
    val selectedMovie: LiveData<MovieProperty>
        get() = _selectedMovie

    // Initialize the _selectedMovie MutableLiveData
    init {
        _selectedMovie.value = movieProperty
    }

//    // The displayPropertyPrice formatted Transformation Map LiveData, which displays the sale
//    // or rental price.
//    val displayPropertyPrice = Transformations.map(selectedMovie) {
//        app.applicationContext.getString(
//            when (it.isRental) {
//                true -> R.string.display_price_monthly_rental
//                false -> R.string.display_price
//            }, it.price)
//    }

//    // The displayPropertyType formatted Transformation Map LiveData, which displays the
//    // "For Rent/Sale" String
//    val displayPropertyType = Transformations.map(selectedProperty) {
//        app.applicationContext.getString(R.string.display_type,
//            app.applicationContext.getString(
//                when(it.isRental) {
//                    true -> R.string.type_rent
//                    false -> R.string.type_sale
//                }))
//    }
}