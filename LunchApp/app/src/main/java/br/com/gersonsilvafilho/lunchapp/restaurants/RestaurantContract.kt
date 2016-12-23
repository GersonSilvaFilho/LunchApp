package br.com.gersonsilvafilho.lunchapp.restaurants

import br.com.gersonsilvafilho.lunchapp.data.Restaurant
import java.util.*

/**
 * This specifies the contract between the view and the presenter.
 */
interface RestaurantContract {
    interface View {

        fun setProgressIndicator(active: Boolean)

        fun showRestaurants(restaurants: List<Restaurant>)

        fun showRestaurantDetailUi(RestaurantId: String)

        fun showSnackbarText(textToShow: String)

        fun showEmptyTextView(active: Boolean)
    }

    interface UserActionsListener {

        fun loadRestaurants(date: Date, forceUpdate: Boolean)

        fun openRestaurantDetails(requestedRestaurant: Restaurant)

    }
}
