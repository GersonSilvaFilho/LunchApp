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

package br.com.gersonsilvafilho.lunchapp.restaurantDetail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.UUID;

import br.com.gersonsilvafilho.lunchapp.data.Restaurant;
import br.com.gersonsilvafilho.lunchapp.data.RestaurantRepository;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link RestaurantDetailPresenter}
 */
public class RestaurantDetailPresenterTest {

    public static final String INVALID_ID = "INVALID_ID";

    public static final String TITLE_TEST = "title";

    public static final String DESCRIPTION_TEST = "description";

    public static final Date DATE = new Date();

    @Mock
    private RestaurantRepository mRestaurantsRepository;

    @Mock
    private RestaurantDetailContract.View mRestaurantDetailView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<RestaurantRepository.GetRestaurantCallback> mGetRestaurantCallbackCaptor;

    private RestaurantDetailPresenter mRestaurantsDetailsPresenter;

    @Before
    public void setupRestaurantsPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mRestaurantsDetailsPresenter = new RestaurantDetailPresenter(mRestaurantsRepository, mRestaurantDetailView);
    }

    @Test
    public void getRestaurantFromRepositoryAndLoadIntoView() {
        // Given an initialized RestaurantDetailPresenter with stubbed note
        Restaurant note = new Restaurant(UUID.randomUUID().toString(), TITLE_TEST, DESCRIPTION_TEST);

        // When notes presenter is asked to open a note
        mRestaurantsDetailsPresenter.openRestaurant(note.getId(), DATE);

        // Then note is loaded from model, callback is captured and progress indicator is shown
        verify(mRestaurantsRepository).getRestaurant(eq(note.getId()), eq(DATE), mGetRestaurantCallbackCaptor.capture());
        verify(mRestaurantDetailView).setProgressIndicator(true);

        // When note is finally loaded
        mGetRestaurantCallbackCaptor.getValue().onRestaurantsLoaded(note); // Trigger callback

        // Then progress indicator is hidden and title and description are shown in UI
        verify(mRestaurantDetailView).setProgressIndicator(false);
        verify(mRestaurantDetailView).showTitle(TITLE_TEST);
        verify(mRestaurantDetailView).showDescription(DESCRIPTION_TEST);
    }

    @Test
    public void getUnknownRestaurantFromRepositoryAndLoadIntoView() {
        // When loading of a note is requested with an invalid note ID.
        mRestaurantsDetailsPresenter.openRestaurant(INVALID_ID, DATE);

        // Then note with invalid id is attempted to load from model, callback is captured and
        // progress indicator is shown.
        verify(mRestaurantDetailView).setProgressIndicator(true);
        verify(mRestaurantsRepository).getRestaurant(eq(INVALID_ID), eq(DATE), mGetRestaurantCallbackCaptor.capture());

        // When note is finally loaded
        mGetRestaurantCallbackCaptor.getValue().onRestaurantsLoaded(null); // Trigger callback

        // Then progress indicator is hidden and missing note UI is shown
        verify(mRestaurantDetailView).setProgressIndicator(false);
        verify(mRestaurantDetailView).showMissingRestaurant();
    }
}
