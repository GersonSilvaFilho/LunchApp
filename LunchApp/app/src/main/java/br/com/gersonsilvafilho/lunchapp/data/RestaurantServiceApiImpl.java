package br.com.gersonsilvafilho.lunchapp.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Implementation of the Restaurants Service API that calls near restaurants using GooglePlaceAPI
 */
public class RestaurantServiceApiImpl implements RestaurantServiceApi, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private String LOG_TAG = "RestaurantService";
    private VotesService votesService;


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

        //Create vote service Instance
        votesService = new VotesService();
    }

    @Override
    public void getAllRestaurant(final Date date, final RestaurantsServiceCallback callback) {

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
                 final List<Restaurant> restaurants = new ArrayList<>();

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
                        Restaurant restaurant = new Restaurant(placeLikelihood.getPlace().getId(), placeLikelihood.getPlace().getName().toString()
                                , placeLikelihood.getPlace().getPhoneNumber().toString());
                        restaurants.add(restaurant);
                    }
                }

                //Get votes from the other API
                votesService.GetVotesFromDay(date, new Callback<Map<String,List<String>>>() {
                            @Override
                            public void onResponse(Response<Map<String,List<String>>> response, Retrofit retrofit) {
                                if(response.body() == null )
                                {
                                    callback.onLoaded(restaurants);
                                    return;
                                }

                                for (Restaurant restaurant: restaurants)
                                {
                                    for(Map.Entry<String, List<String>> vote: response.body().entrySet())
                                    {
                                        if(vote.getKey().equals(restaurant.getId()))
                                        {
                                            restaurant.incrementVotes(vote.getValue().size());
                                        }
                                    }
                                }
                                callback.onLoaded(restaurants);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                callback.onLoaded(restaurants);
                            }
                        });
                //release object
                likelyPlaces.release();
            }
        });
    }

    @Override
    public void getRestaurant(final String restaurantId, final RestaurantsServiceCallback callback) {
        PendingResult<PlaceBuffer> result = Places.GeoDataApi.getPlaceById(mGoogleApiClient, restaurantId);
        result.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if( places.getStatus().isSuccess() )
                {
                    for( Place place : places )
                    {
                        Restaurant restaurant = new Restaurant(place.getId(), place.getName().toString(), place.getAddress().toString());
                        callback.onLoaded(restaurant);
                    }
                }
            }
        });
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
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

    @Override
    public void getRestaurantImageBitmap(@NotNull String RestaurantId, final int imgHeight, final int imageWidth, @NotNull final RestaurantsServiceCallback<Bitmap> callback) {
        PendingResult<PlacePhotoMetadataResult> result = Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, RestaurantId);
        result.setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
            @Override
            public void onResult(@NonNull PlacePhotoMetadataResult placePhotoMetadataResult) {
                if (!placePhotoMetadataResult.getStatus().isSuccess()) {
                    return;
                }

                PlacePhotoMetadataBuffer photoMetadataBuffer = placePhotoMetadataResult.getPhotoMetadata();
                if (photoMetadataBuffer.getCount() > 0) {
                    // Display the first bitmap in an ImageView in the size of the view
                    photoMetadataBuffer.get(0)
                            .getScaledPhoto(mGoogleApiClient, imageWidth,imgHeight)
                            .setResultCallback(new ResultCallback<PlacePhotoResult>() {
                                @Override
                                public void onResult(PlacePhotoResult placePhotoResult) {
                                    if (!placePhotoResult.getStatus().isSuccess()) {
                                        return;
                                    }
                                    callback.onLoaded(placePhotoResult.getBitmap());
                                }
                            });
                }
            }
        });
    }

    @Override
    public void sendRestaurantVote(@NotNull String restaurantId, @NotNull String userId, @NotNull final RestaurantsServiceCallback<Map<String,String>> callback) {
        Vote vote = new Vote(userId, restaurantId, new Date().getTime()/1000);
        votesService.SendVote(vote, new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Response<Map<String,String>> response, Retrofit retrofit) {
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                //TODO
                callback.onLoaded(null);
            }
        });
    }
}
