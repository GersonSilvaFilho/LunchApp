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

package br.com.gersonsilvafilho.lunchapp.restaurantDetail

import br.com.gersonsilvafilho.lunchapp.data.Restaurant
import br.com.gersonsilvafilho.lunchapp.data.RestaurantRepository

import com.google.common.base.Preconditions.checkNotNull

/**
 * Listens to user actions from the UI ([RestaurantDetailFragment]), retrieves the data and updates
 * the UI as required.
 */
class RestaurantDetailPresenter(RestaurantsRepository: RestaurantRepository,
                                RestaurantDetailView: RestaurantDetailContract.View) : RestaurantDetailContract.UserActionsListener {

    private val mRestaurantsRepository: RestaurantRepository

    private val mRestaurantsDetailView: RestaurantDetailContract.View

    init {
        mRestaurantsRepository = checkNotNull(RestaurantsRepository, "RestaurantsRepository cannot be null!")
        mRestaurantsDetailView = checkNotNull(RestaurantDetailView, "RestaurantDetailView cannot be null!")
    }

    override fun openRestaurant(RestaurantId: String?) {
        if (null == RestaurantId || RestaurantId.isEmpty()) {
            mRestaurantsDetailView.showMissingRestaurant()
            return
        }

        mRestaurantsDetailView.setProgressIndicator(true)
        mRestaurantsRepository.getRestaurant(RestaurantId) { Restaurant ->
            mRestaurantsDetailView.setProgressIndicator(false)
            if (null == Restaurant) {
                mRestaurantsDetailView.showMissingRestaurant()
            } else {
                showRestaurant(Restaurant)
            }
        }
    }

    private fun showRestaurant(Restaurant: Restaurant) {
        val title = Restaurant.title
        val description = Restaurant.description
        val imageUrl = Restaurant.imageUrl

        if (title != null && title.isEmpty()) {
            mRestaurantsDetailView.hideTitle()
        } else {
            mRestaurantsDetailView.showTitle(title.toString())
        }

        if (description != null && description.isEmpty()) {
            mRestaurantsDetailView.hideDescription()
        } else {
            mRestaurantsDetailView.showDescription(description.toString())
        }

        if (imageUrl != null) {
            mRestaurantsDetailView.showImage(imageUrl)
        } else {
            mRestaurantsDetailView.hideImage()
        }

    }
}
