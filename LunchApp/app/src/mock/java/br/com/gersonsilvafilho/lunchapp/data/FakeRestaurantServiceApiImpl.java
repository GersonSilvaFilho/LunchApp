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

import android.graphics.Bitmap;
import android.support.v4.util.ArrayMap;

import com.google.common.collect.Lists;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class FakeRestaurantServiceApiImpl implements RestaurantServiceApi {

    // TODO replace this with a new test specific data set.
    private static final ArrayMap<String, Restaurant> RESTAURANT_SERVICE_DATA = new ArrayMap();

    @Override
    public void saveRestaurant(Restaurant Restaurant) {
        RESTAURANT_SERVICE_DATA.put(Restaurant.getId(), Restaurant);
    }

    @Override
    public void getAllRestaurant(@NotNull Date date, @NotNull RestaurantsServiceCallback<List<Restaurant>> callback) {
        callback.onLoaded(Lists.newArrayList(RESTAURANT_SERVICE_DATA.values()));
    }

    @Override
    public void getRestaurantImageBitmap(@NotNull String RestaurantId, int imgHeight, int imageWidth, @NotNull RestaurantsServiceCallback<Bitmap> callback) {

    }

    @Override
    public void sendRestaurantVote(@NotNull String restaurantId, @NotNull String userId, @NotNull Date date, @NotNull RestaurantsServiceCallback<Map<String, String>> callback) {

    }

    @Override
    public void getRestaurant(@NotNull String RestaurantId, @NotNull Date date, @NotNull RestaurantsServiceCallback<Restaurant> callback) {
        Restaurant Restaurant = RESTAURANT_SERVICE_DATA.get(RestaurantId);
        callback.onLoaded(Restaurant);
    }
}
