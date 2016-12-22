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

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the Restaurants Service API that adds a latency simulating network.
 */
public class RestaurantServiceApiImpl implements RestaurantServiceApi, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int SERVICE_LATENCY_IN_MILLIS = 0;
    private static final ArrayMap<String, Restaurant> RESTAURANT_SERVICE_DATA =
            RestaurantServiceApiEndpoint.loadPersistedRestaurants();

    private GoogleApiClient mGoogleApiClient;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private String LOG_TAG = "RestaurantService";


    public RestaurantServiceApiImpl(Context context)
    {
        mGoogleApiClient = new GoogleApiClient
                .Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        if (mGoogleApiClient.isConnected()) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            } else {
                callPlaceDetectionApi();
            }

        }
    }

    private void callPlaceDetectionApi() throws SecurityException {

    }

    @Override
    public void getAllRestaurant(final RestaurantsServiceCallback callback) {

        LatLngBounds bounds = new LatLngBounds( new LatLng( 39.906374, -105.122337 ), new LatLng( 39.949552, -105.068779 ) );

        AutocompleteFilter autocompleteFilter=new AutocompleteFilter.Builder().setTypeFilter(Place.TYPE_RESTAURANT).build();
        PendingResult<AutocompletePredictionBuffer> pendingResult=Places
                .GeoDataApi
                .getAutocompletePredictions(mGoogleApiClient,null, bounds, null);

        pendingResult.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
            @Override
            public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                Status status=autocompletePredictions.getStatus();
//                Iterator<AutocompletePrediction> iterator=autocompletePredictions.iterator();
                List<Restaurant> restaurants = new ArrayList<>();
//                while (iterator.hasNext()){
//                    AutocompletePrediction autocompletePrediction=iterator.next();
//                    // do something
//
//                }

                if( autocompletePredictions.getStatus().isSuccess() ) {
                    for( AutocompletePrediction prediction : autocompletePredictions ) {
                        //Add as a new item to avoid IllegalArgumentsException when buffer is released
                        Restaurant restaurant = new Restaurant(prediction.getPrimaryText(null).toString()
                                ,prediction.getFullText(null).toString());
                        restaurants.add(restaurant);
                    }
                }
                callback.onLoaded(restaurants);
            }
        }, 20, TimeUnit.SECONDS);
        //rectangleLyon is LatLngBounds, to remove filters put autocompletefilter as null
        // Second parameter(as String "delhi") is your search query




//        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
//            @Override
//            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
//                 List<Restaurant> restaurants = new ArrayList<>();
//
//                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                    Log.i(LOG_TAG, String.format("Place '%s' with " +
//                                    "likelihood: %g",
//                            placeLikelihood.getPlace().getName(),
//                            placeLikelihood.getLikelihood()));
//                    Restaurant restaurant = new Restaurant(placeLikelihood.getPlace().getName().toString()
//                                                          ,placeLikelihood.getPlace().getAddress().toString());
//                    restaurants.add(restaurant);
//                }
//
//                callback.onLoaded(restaurants);
//                likelyPlaces.release();
//            }
//        });

//        // Simulate network by delaying the execution.
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                List<Restaurant> restaurants = new ArrayList<>(RESTAURANT_SERVICE_DATA.values());
//                callback.onLoaded(restaurants);
//            }
//        }, SERVICE_LATENCY_IN_MILLIS);
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
