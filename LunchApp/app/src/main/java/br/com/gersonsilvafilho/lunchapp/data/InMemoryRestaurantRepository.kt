package br.com.gersonsilvafilho.lunchapp.data

import android.graphics.Bitmap
import android.support.annotation.VisibleForTesting
import com.google.common.base.Preconditions.checkNotNull
import com.google.common.collect.ImmutableList
import java.util.*

/**
 * Concrete implementation to load Restaurants from the a data source.
 */
class InMemoryRestaurantRepository(restaurantServiceApi: RestaurantServiceApi) : RestaurantRepository {

    private val mRestaurantServiceApi: RestaurantServiceApi

    /**
     * This method has reduced visibility for testing and is only visible to tests in the same
     * package.
     */
    @VisibleForTesting
    var mCachedRestaurants: List<Restaurant>? = null

    init {
        mRestaurantServiceApi = checkNotNull(restaurantServiceApi)
    }

    override fun getRestaurants(date: Date, callback: RestaurantRepository.LoadRestaurantsCallback) {
        checkNotNull(callback)
        // Load from API only if needed.
        if (mCachedRestaurants == null) {
            mRestaurantServiceApi.getAllRestaurant(date, object : RestaurantServiceApi.RestaurantsServiceCallback<List<Restaurant>> {
                override fun onLoaded(restaurants: List<Restaurant>) {
                    mCachedRestaurants = ImmutableList.copyOf(restaurants)
                    callback.onRestaurantsLoaded(mCachedRestaurants!!)
                }
            })
        } else {
            callback.onRestaurantsLoaded(mCachedRestaurants!!)
        }
    }

    override fun saveRestaurant(restaurant: Restaurant) {
        checkNotNull(restaurant)
        mRestaurantServiceApi.saveRestaurant(restaurant)
        refreshData()
    }

    override fun getRestaurant(RestaurantId: String, callback: RestaurantRepository.GetRestaurantCallback) {
        checkNotNull(RestaurantId)
        checkNotNull(callback)
        // Load Restaurants matching the id always directly from the API.
        mRestaurantServiceApi.getRestaurant(RestaurantId, object : RestaurantServiceApi.RestaurantsServiceCallback<Restaurant> {
            override fun onLoaded(restaurant: Restaurant) {
                callback.onRestaurantsLoaded(restaurant)
            }
        })
    }

    override fun refreshData() {
        mCachedRestaurants = null
    }

    override fun getRestaurantImageBitmap(RestaurantId: String, imgHeight: Int, imageWidth: Int, callback: RestaurantRepository.GetRestaurantImageCallback) {
        checkNotNull(String)
        checkNotNull(callback)
        // Load Restaurants matching the id always directly from the API.
        mRestaurantServiceApi.getRestaurantImageBitmap(RestaurantId, imgHeight, imageWidth, object : RestaurantServiceApi.RestaurantsServiceCallback<Bitmap>
        {
            override fun onLoaded(bitmap: Bitmap) {
                callback.onRestaurantImageLoaded(bitmap)
            }
        })
    }

    override fun sendRestaurantVote(RestaurantId: String, UserId: String, date: Date, callback: RestaurantRepository.SendRestaurantVoteCallback)
    {
        checkNotNull(String)
        checkNotNull(callback)

        mRestaurantServiceApi.sendRestaurantVote(RestaurantId, UserId, date, object : RestaurantServiceApi.RestaurantsServiceCallback<Map<String,String>>
        {
            override fun onLoaded(voteIsOk: Map<String,String>)
            {
                callback.onRestaurantVote(voteIsOk)
            }
        })
    }

}
