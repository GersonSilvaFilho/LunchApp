/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.gersonsilvafilho.lunchapp.restaurants


import br.com.gersonsilvafilho.lunchapp.data.Restaurant
import br.com.gersonsilvafilho.lunchapp.data.RestaurantRepository
import br.com.gersonsilvafilho.lunchapp.util.EspressoIdlingResource
import com.google.common.base.Preconditions.checkNotNull
import java.util.*


/**
 * Listens to user actions from the UI ([RestaurantsFragment]), retrieves the data and updates the
 * UI as required.
 */
class RestaurantPresenter(restaurantRepository: RestaurantRepository, restaurantView: RestaurantContract.View) : RestaurantContract.UserActionsListener {

    private val mRestaurantRepository: RestaurantRepository
    private val mRestaurantView: RestaurantContract.View

    init {
        mRestaurantRepository = checkNotNull<RestaurantRepository>(restaurantRepository, "restaurantRepository cannot be null")
        mRestaurantView = checkNotNull<RestaurantContract.View>(restaurantView, "restaurantView cannot be null!")
    }

    override fun loadRestaurants(date: Date, forceUpdate: Boolean) {
        mRestaurantView.setProgressIndicator(true)
        if (forceUpdate) {
            mRestaurantRepository.refreshData()
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment() // App is busy until further notice


        mRestaurantRepository.getRestaurants(date, object : RestaurantRepository.LoadRestaurantsCallback {
            override fun onRestaurantsLoaded(Restaurants: List<Restaurant>) {
                EspressoIdlingResource.decrement() // Set app as idle.
                mRestaurantView.setProgressIndicator(false)
                mRestaurantView.showRestaurants(Restaurants)
            }
        })
    }

    override fun openRestaurantDetails(requestedRestaurant: Restaurant) {
        checkNotNull<Restaurant>(requestedRestaurant, "requestedRestaurant cannot be null!")
        mRestaurantView.showRestaurantDetailUi(requestedRestaurant.id)
    }

}
