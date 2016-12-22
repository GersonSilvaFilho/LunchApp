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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of the Restaurants Service API that calls near restaurants using GooglePlaceAPI
 */
public class RestaurantServiceApiImpl implements RestaurantServiceApi, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final ArrayMap<String, Restaurant> RESTAURANT_SERVICE_DATA =
            RestaurantServiceApiEndpoint.loadPersistedRestaurants();

    private GoogleApiClient mGoogleApiClient;
    private String LOG_TAG = "RestaurantService";


    public RestaurantServiceApiImpl(Context context)
    {
        //Create googleApiClientInstance
        mGoogleApiClient = new GoogleApiClient
                .Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void getAllRestaurant(final RestaurantsServiceCallback callback) {

        final Collection<Integer> filterTypes = new ArrayList<Integer>();
        filterTypes.add(Place.TYPE_RESTAURANT);
        filterTypes.add(Place.TYPE_CAFE);
        filterTypes.add(Place.TYPE_ESTABLISHMENT);
        filterTypes.add(Place.TYPE_BAKERY);
        filterTypes.add(Place.TYPE_BAR);

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);

        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                 List<Restaurant> restaurants = new ArrayList<>();

                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    if(placeLikelihood.getPlace().getPlaceTypes().contains(Place.TYPE_RESTAURANT) ||
                            placeLikelihood.getPlace().getPlaceTypes().contains(Place.TYPE_CAFE) ||
                            placeLikelihood.getPlace().getPlaceTypes().contains(Place.TYPE_BAKERY) ||
                            placeLikelihood.getPlace().getPlaceTypes().contains(Place.TYPE_BAR))
                    {
                        Log.i(LOG_TAG, String.format("Place '%s' with " +
                                        "likelihood: %g",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                        Restaurant restaurant = new Restaurant(placeLikelihood.getPlace().getName().toString()
                                , placeLikelihood.getPlace().getPhoneNumber().toString());
                        restaurants.add(restaurant);
                    }
                }

                callback.onLoaded(restaurants);
                //release object
                likelyPlaces.release();
            }
        });

//        AutocompleteFilter autocompleteFilter=new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_RESTAURANT).build();
//
//
//        Places.GeoDataApi
//                .getAutocompletePredictions(mGoogleApiClient,"Restaurante", bounds, autocompleteFilter).setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
//            @Override
//            public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
//                Status status=autocompletePredictions.getStatus();
////                Iterator<AutocompletePrediction> iterator=autocompletePredictions.iterator();
//                List<Restaurant> restaurants = new ArrayList<>();
////                while (iterator.hasNext()){
////                    AutocompletePrediction autocompletePrediction=iterator.next();
////                    // do something
////
////                }
//
//                if( autocompletePredictions.getStatus().isSuccess() ) {
//                    for( AutocompletePrediction prediction : autocompletePredictions ) {
//                        //Add as a new item to avoid IllegalArgumentsException when buffer is released
//                        Restaurant restaurant = new Restaurant(prediction.getPrimaryText(null).toString()
//                                ,prediction.getFullText(null).toString());
//                        restaurants.add(restaurant);
//                    }
//                }
//                callback.onLoaded(restaurants);
//            }
//        }, 20, TimeUnit.SECONDS);

    }

    @Override
    public void getRestaurant(final String restaurantId, final RestaurantsServiceCallback callback) {
        Restaurant Restaurant = RESTAURANT_SERVICE_DATA.get(restaurantId);
        callback.onLoaded(Restaurant);
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        RESTAURANT_SERVICE_DATA.put(restaurant.getId(), restaurant);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(LOG_TAG, "Google Places API onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG_TAG, "Google Places API onConnectionSuspended failed with error code: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());
    }
}
