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

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
public class InMemoryRestaurantRepositoryTest {

    private final static String RESTAURANT_TITLE = "title";

    private static List<Restaurant> RESTAURANTS = Lists.newArrayList(
            new Restaurant("Restaurant1", "Description1"),
            new Restaurant("Restaurant2", "Description2"),
            new Restaurant("Restaurant3", "Description3"),
            new Restaurant("Restaurant4", "Description4"));

    private InMemoryRestaurantRepository mRestaurantsRepository;



    @Mock
    private RestaurantRepository.GetRestaurantCallback mGetRestaurantCallback;

    @Mock
    private RestaurantRepository.LoadRestaurantsCallback mLoadRestaurantsCallback;

    @Mock
    private RestaurantServiceApiImpl mServiceApi;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<RestaurantServiceApi.RestaurantsServiceCallback> mRestaurantsServiceCallbackCaptor;

    @Before
    public void setupRestaurantsRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mRestaurantsRepository = new InMemoryRestaurantRepository(mServiceApi);
    }

    @Test
    public void getRestaurants_repositoryCachesAfterFirstApiCall() {
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the notes repository
        twoLoadCallsToRepository(mLoadRestaurantsCallback);

        // Then notes where only requested once from Service API
        verify(mServiceApi).getAllRestaurant(any(RestaurantServiceApi.RestaurantsServiceCallback.class));
    }

    @Test
    public void invalidateCache_DoesNotCallTheServiceApi() {
        // Given a setup Captor to capture callbacks
        twoLoadCallsToRepository(mLoadRestaurantsCallback);

        // When data refresh is requested
        mRestaurantsRepository.refreshData();
        mRestaurantsRepository.getRestaurants(mLoadRestaurantsCallback); // Third call to API

        // The notes where requested twice from the Service API (Caching on first and third call)
        verify(mServiceApi, times(2)).getAllRestaurant(any(RestaurantServiceApi.RestaurantsServiceCallback.class));
    }

    @Test
    public void getRestaurants_requestsAllRestaurantsFromServiceApi() {
        // When notes are requested from the notes repository
        mRestaurantsRepository.getRestaurants(mLoadRestaurantsCallback);

        // Then notes are loaded from the service API
        verify(mServiceApi).getAllRestaurant(any(RestaurantServiceApi.RestaurantsServiceCallback.class));
    }

    @Test
    public void saveRestaurant_savesRestaurantToServiceAPIAndInvalidatesCache() {
        // Given a stub note with title and description
        Restaurant newRestaurant = new Restaurant(RESTAURANT_TITLE, "Some Restaurant Description");

        // When a note is saved to the notes repository
        mRestaurantsRepository.saveRestaurant(newRestaurant);

        // Then the notes cache is cleared
        assertThat(mRestaurantsRepository.getMCachedRestaurants(), is(nullValue()));
    }

    @Test
    public void getRestaurant_requestsSingleRestaurantFromServiceApi() {
        // When a note is requested from the notes repository
        mRestaurantsRepository.getRestaurant(RESTAURANT_TITLE, mGetRestaurantCallback);

        // Then the note is loaded from the service API
        verify(mServiceApi).getRestaurant(eq(RESTAURANT_TITLE), any(RestaurantServiceApi.RestaurantsServiceCallback.class));
    }

    /**
     * Convenience method that issues two calls to the notes repository
     */
    private void twoLoadCallsToRepository(RestaurantRepository.LoadRestaurantsCallback callback) {
        // When notes are requested from repository
        mRestaurantsRepository.getRestaurants(callback); // First call to API

        // Use the Mockito Captor to capture the callback
        verify(mServiceApi).getAllRestaurant(mRestaurantsServiceCallbackCaptor.capture());

        // Trigger callback so notes are cached
        mRestaurantsServiceCallbackCaptor.getValue().onLoaded(RESTAURANTS);

        mRestaurantsRepository.getRestaurants(callback); // Second call to API
    }

}