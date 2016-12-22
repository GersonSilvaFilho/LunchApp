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

import android.support.v4.util.ArrayMap;

/**
 * This is the endpoint for your data source. Typically, it would be a SQLite db and/or a server
 * API. In this example, we fake this by creating the data on the fly.
 */
public final class RestaurantServiceApiEndpoint {

    static {
        DATA = new ArrayMap(2);
        addRestaurant("Restaurant1", "Good Price", null);
        addRestaurant("Restaurant2", "Good food", null);
        addRestaurant("Restaurant3", "Really close", null);
        addRestaurant("Restaurant4", "Good mood", null);
    }

    private final static ArrayMap<String, Restaurant> DATA;

    private static void addRestaurant(String title, String description, String imageUrl) {
        Restaurant newRestaurant = new Restaurant(title, description, imageUrl);
        DATA.put(newRestaurant.getId(), newRestaurant);
    }

    /**
     * @return the Restaurants to show when starting the app.
     */
    public static ArrayMap<String, Restaurant> loadPersistedRestaurants() {
        return DATA;
    }
}
