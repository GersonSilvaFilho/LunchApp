
package br.com.gersonsilvafilho.lunchapp.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load Restaurants from the a data source.
 */
public class InMemoryRestaurantRepository implements RestaurantRepository {

    private final RestaurantServiceApi mRestaurantServiceApi;

    /**
     * This method has reduced visibility for testing and is only visible to tests in the same
     * package.
     */
    @VisibleForTesting
    List<Restaurant> mCachedRestaurants;

    public InMemoryRestaurantRepository(@NonNull RestaurantServiceApi restaurantServiceApi) {
        mRestaurantServiceApi = checkNotNull(restaurantServiceApi);
    }

    @Override
    public void getRestaurants(@NonNull final LoadRestaurantsCallback callback) {
        checkNotNull(callback);
        // Load from API only if needed.
        if (mCachedRestaurants == null) {
            mRestaurantServiceApi.getAllRestaurant(new RestaurantServiceApi.RestaurantsServiceCallback<List<Restaurant>>()
            {
                @Override
                public void onLoaded(List<Restaurant> restaurants) {
                    mCachedRestaurants = ImmutableList.copyOf(restaurants);
                    callback.onRestaurantsLoaded(mCachedRestaurants);
                }
            });
        } else {
            callback.onRestaurantsLoaded(mCachedRestaurants);
        }
    }

    @Override
    public void saveRestaurant(@NonNull Restaurant restaurant) {
        checkNotNull(restaurant);
        mRestaurantServiceApi.saveRestaurant(restaurant);
        refreshData();
    }

    @Override
    public void getRestaurant(@NonNull final String RestaurantId, @NonNull final GetRestaurantCallback callback) {
        checkNotNull(RestaurantId);
        checkNotNull(callback);
        // Load Restaurants matching the id always directly from the API.
        mRestaurantServiceApi.getRestaurant(RestaurantId, new RestaurantServiceApi.RestaurantsServiceCallback<Restaurant>()
        {
            @Override
            public void onLoaded(Restaurant restaurant) {
                callback.onRestaurantsLoaded(restaurant);
            }
        });
    }

    @Override
    public void refreshData() {
        mCachedRestaurants = null;
    }

}
