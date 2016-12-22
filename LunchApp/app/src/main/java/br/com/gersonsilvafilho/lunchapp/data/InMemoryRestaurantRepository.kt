package br.com.gersonsilvafilho.lunchapp.data

import android.support.annotation.VisibleForTesting
import com.google.common.base.Preconditions.checkNotNull
import com.google.common.collect.ImmutableList

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

    override fun getRestaurants(callback: RestaurantRepository.LoadRestaurantsCallback) {
        checkNotNull(callback)
        // Load from API only if needed.
        if (mCachedRestaurants == null) {
            mRestaurantServiceApi.getAllRestaurant(object : RestaurantServiceApi.RestaurantsServiceCallback<List<Restaurant>> {
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

}
