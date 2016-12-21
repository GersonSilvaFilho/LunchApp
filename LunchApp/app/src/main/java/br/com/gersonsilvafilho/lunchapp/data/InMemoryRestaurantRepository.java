
package br.com.gersonsilvafilho.lunchapp.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load notes from the a data source.
 */
public class InMemoryRestaurantRepository implements RestaurantRepository {

    private final RestaurantServiceApi mRestaurantServiceApi;

    /**
     * This method has reduced visibility for testing and is only visible to tests in the same
     * package.
     */
    @VisibleForTesting
    List<Restaurant> mCachedNotes;

    public InMemoryRestaurantRepository(@NonNull RestaurantServiceApi restaurantServiceApi) {
        mRestaurantServiceApi = checkNotNull(restaurantServiceApi);
    }

    @Override
    public void getRestaurants(@NonNull final LoadRestaurantsCallback callback) {
        checkNotNull(callback);
        // Load from API only if needed.
        if (mCachedNotes == null) {
            mRestaurantServiceApi.getAllRestaurant(new RestaurantServiceApi.RestaurantsServiceCallback<List<Restaurant>>()
            {
                @Override
                public void onLoaded(List<Restaurant> restaurants) {
                    mCachedNotes = ImmutableList.copyOf(restaurants);
                    callback.onRestaurantsLoaded(mCachedNotes);
                }
            });
        } else {
            callback.onRestaurantsLoaded(mCachedNotes);
        }
    }

    @Override
    public void saveRestaurant(@NonNull Restaurant restaurant) {
        checkNotNull(restaurant);
        mRestaurantServiceApi.saveRestaurant(restaurant);
        refreshData();
    }

    @Override
    public void getRestaurant(@NonNull final String noteId, @NonNull final GetRestaurantCallback callback) {
        checkNotNull(noteId);
        checkNotNull(callback);
        // Load notes matching the id always directly from the API.
        mRestaurantServiceApi.getRestaurant(noteId, new RestaurantServiceApi.RestaurantsServiceCallback<Restaurant>()
        {
            @Override
            public void onLoaded(Restaurant restaurant) {
                callback.onRestaurantsLoaded(restaurant);
            }
        });
    }

    @Override
    public void refreshData() {
        mCachedNotes = null;
    }

}
