package br.com.gersonsilvafilho.lunchapp.data

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlacePhotoMetadataResult
import com.google.android.gms.location.places.PlacePhotoResult
import com.google.android.gms.location.places.Places
import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit
import java.util.*

/**
 * Implementation of the Restaurants Service API that calls near restaurants using GooglePlaceAPI
 */
class RestaurantServiceApiImpl(context: Context) : RestaurantServiceApi, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private val mGoogleApiClient: GoogleApiClient
    private val LOG_TAG = "RestaurantService"
    private val votesService: VotesService


    init {
        //Create googleApiClientInstance
        mGoogleApiClient = GoogleApiClient.Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()

        mGoogleApiClient.connect()

        //Create vote service Instance
        votesService = VotesService()
    }

    override fun saveRestaurant(restaurant: Restaurant) {
    }

    override fun onConnected(bundle: Bundle?) {
        Log.e(LOG_TAG, "Google Places API onConnected")
    }

    override fun onConnectionSuspended(i: Int) {
        Log.e(LOG_TAG, "Google Places API onConnectionSuspended failed with error code: ")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: " + connectionResult.errorCode)
    }

    override fun getRestaurantImageBitmap(RestaurantId: String, imgHeight: Int, imageWidth: Int, callback: RestaurantServiceApi.RestaurantsServiceCallback<Bitmap>) {
        val result = Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, RestaurantId)
        result.setResultCallback(ResultCallback<PlacePhotoMetadataResult> { placePhotoMetadataResult ->
            if (!placePhotoMetadataResult.status.isSuccess) {
                return@ResultCallback
            }

            val photoMetadataBuffer = placePhotoMetadataResult.photoMetadata
            if (photoMetadataBuffer.count > 0) {
                // Display the first bitmap in an ImageView in the size of the view
                photoMetadataBuffer.get(0)
                        .getScaledPhoto(mGoogleApiClient, imageWidth, imgHeight)
                        .setResultCallback(ResultCallback<PlacePhotoResult> { placePhotoResult ->
                            if (!placePhotoResult.status.isSuccess) {
                                return@ResultCallback
                            }
                            callback.onLoaded(placePhotoResult.bitmap)
                        })
            }
            photoMetadataBuffer.release()
        })
    }

    override fun sendRestaurantVote(restaurantId: String, userId: String, date: Date, callback: RestaurantServiceApi.RestaurantsServiceCallback<Map<String, String>>) {
        val vote = Vote(userId, restaurantId, date.time / 1000)
        votesService.SendVote(vote, object : Callback<Map<String, String>> {
            override fun onResponse(response: Response<Map<String, String>>, retrofit: Retrofit) {
                callback.onLoaded(response.body())
            }

            override fun onFailure(t: Throwable) {
                //TODO
                callback.onLoaded(null)
            }
        })
    }

    override fun getAllRestaurant(date: Date, callback: RestaurantServiceApi.RestaurantsServiceCallback<List<Restaurant>>) {
        val result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null)

        result.setResultCallback { likelyPlaces ->
            val restaurants = ArrayList<Restaurant>()

            for (placeLikelihood in likelyPlaces) {
                Log.i(LOG_TAG, String.format("Place '%s' with " + "likelihood: %g",
                        placeLikelihood.place.name,
                        placeLikelihood.likelihood))
                if (placeLikelihood.place.placeTypes.contains(Place.TYPE_RESTAURANT) ||
                        placeLikelihood.place.placeTypes.contains(Place.TYPE_CAFE) ||
                        placeLikelihood.place.placeTypes.contains(Place.TYPE_BAKERY) ||
                        placeLikelihood.place.placeTypes.contains(Place.TYPE_BAR)) {

                    val restaurant = Restaurant(placeLikelihood.place.id, placeLikelihood.place.name.toString(), placeLikelihood.place.phoneNumber.toString())
                    restaurants.add(restaurant)
                }
            }

            //Get votes from the other API
            votesService.GetVotesFromDay(date, object : Callback<Map<String, List<String>>> {
                override fun onResponse(response: Response<Map<String, List<String>>>, retrofit: Retrofit) {
                    if (response.body() == null) {
                        callback.onLoaded(restaurants)
                        return
                    }

                    for (restaurant in restaurants) {
                        for ((key, value) in response.body()) {
                            if (key == restaurant.id) {
                                restaurant.incrementVotes(value.size)
                            }
                        }
                    }
                    //This order the restaurants from Most voted to less voted
                    callback.onLoaded(restaurants.sortedDescending())
                }

                override fun onFailure(t: Throwable) {
                    callback.onLoaded(restaurants)
                }
            })
            //release object
            likelyPlaces.release()
        }
    }

    override fun getRestaurant(RestaurantId: String, date: Date, callback: RestaurantServiceApi.RestaurantsServiceCallback<Restaurant>) {
        val result = Places.GeoDataApi.getPlaceById(mGoogleApiClient, RestaurantId)
        result.setResultCallback { places ->
            if (places.status.isSuccess) {

                val restaurant = Restaurant(places[0].id, places[0].name.toString(), places[0].address.toString())
                //Get votes from api
                votesService.GetVotesFromDay(date, object : Callback<Map<String, List<String>>> {
                    override fun onResponse(response: Response<Map<String, List<String>>>, retrofit: Retrofit) {
                        if (response.body() == null) {
                            callback.onLoaded(restaurant)
                            return
                        }

                        for ((key, value) in response.body()) {
                            if (key == restaurant.id) {
                                restaurant.incrementVotes(value.size)
                            }
                        }
                        //This order the restaurants from Most voted to less voted
                        callback.onLoaded(restaurant)
                    }

                    override fun onFailure(t: Throwable) {
                        callback.onLoaded(restaurant)
                    }
                })

                for (place in places) {
                    val restaurant = Restaurant(place.id, place.name.toString(), place.address.toString())
                    callback.onLoaded(restaurant)
                }
            }
        }
    }

}
