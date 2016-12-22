package br.com.gersonsilvafilho.lunchapp.restaurants

import br.com.gersonsilvafilho.lunchapp.data.Restaurant

/**
 * Created by GersonSilva on 12/21/16.
 */

interface RestaurantContract {
    interface View {

        fun setProgressIndicator(active: Boolean)

        fun showRestaurants(restaurants: List<Restaurant>)


        fun showRestaurantDetailUi(RestaurantId: String)
    }

    interface UserActionsListener {

        fun loadRestaurants(forceUpdate: Boolean)

        fun openRestaurantDetails(requestedRestaurant: Restaurant)
    }
}
