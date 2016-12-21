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

package br.com.gersonsilvafilho.lunchapp.data;

import android.os.Handler;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Notes Service API that adds a latency simulating network.
 */
public class RestaurantServiceApiImpl implements RestaurantServiceApi {

    private static final int SERVICE_LATENCY_IN_MILLIS = 0;
    private static final ArrayMap<String, Restaurant> RESTAURANT_SERVICE_DATA =
            RestaurantServiceApiEndpoint.loadPersistedRestaurants();

    @Override
    public void getAllRestaurant(final RestaurantsServiceCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Restaurant> restaurants = new ArrayList<>(RESTAURANT_SERVICE_DATA.values());
                callback.onLoaded(restaurants);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void getRestaurant(final String restaurantId, final RestaurantsServiceCallback callback) {
        //TODO: Add network latency here too.
        Restaurant note = RESTAURANT_SERVICE_DATA.get(restaurantId);
        callback.onLoaded(note);
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        RESTAURANT_SERVICE_DATA.put(restaurant.getId(), restaurant);
    }

}
