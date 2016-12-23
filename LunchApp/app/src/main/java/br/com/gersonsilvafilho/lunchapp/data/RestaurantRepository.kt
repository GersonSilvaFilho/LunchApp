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

package br.com.gersonsilvafilho.lunchapp.data

import android.graphics.Bitmap
import java.util.*

/**
 * Main entry point for accessing Restaurants data.
 */
interface RestaurantRepository {

    interface LoadRestaurantsCallback {

        fun onRestaurantsLoaded(Restaurants: List<Restaurant>)
    }

    interface GetRestaurantCallback {

        fun onRestaurantsLoaded(Restaurant: Restaurant?)
    }

    interface GetRestaurantImageCallback {
        fun onRestaurantImageLoaded(bitmap: Bitmap)
    }

    interface SendRestaurantVoteCallback {
        fun onRestaurantVote(response: Map<String,String>)
    }

    fun getRestaurants(date: Date, callback: LoadRestaurantsCallback)

    fun getRestaurant(RestaurantId: String, callback: GetRestaurantCallback)

    fun saveRestaurant(Restaurant: Restaurant)

    fun refreshData()

    fun getRestaurantImageBitmap(RestaurantId: String, imgHeight: Int, imageWidth: Int, callback: GetRestaurantImageCallback)

    fun sendRestaurantVote(RestaurantId: String, UserId: String, date: Date, callback: SendRestaurantVoteCallback)

}
