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
 * Defines an interface to the service API that is used by this application. All data request should
 * be piped through this interface.
 */
interface RestaurantServiceApi {

    interface RestaurantsServiceCallback<T> {
        fun onLoaded(Restaurants: T)
    }

    fun getAllRestaurant(date: Date, callback: RestaurantsServiceCallback<List<Restaurant>>)

    fun getRestaurant(RestaurantId: String, callback: RestaurantsServiceCallback<Restaurant>)

    fun getRestaurantImageBitmap(RestaurantId: String, imgHeight: Int, imageWidth: Int, callback: RestaurantsServiceCallback<Bitmap>)

    fun saveRestaurant(restaurant: Restaurant)

    fun sendRestaurantVote(restaurantId: String, userId: String, date: Date, callback: RestaurantsServiceCallback<Map<String,String>>)
}
