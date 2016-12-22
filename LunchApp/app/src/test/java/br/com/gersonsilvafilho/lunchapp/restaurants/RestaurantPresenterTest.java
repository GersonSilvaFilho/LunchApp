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

package br.com.gersonsilvafilho.lunchapp.restaurants;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import br.com.gersonsilvafilho.lunchapp.data.Restaurant;
import br.com.gersonsilvafilho.lunchapp.data.RestaurantRepository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;


public class RestaurantPresenterTest {

    private static List<Restaurant> RESTAURANTS = Lists.newArrayList(
            new Restaurant("Restaurant1", "Description1"),
            new Restaurant("Restaurant2", "Description2"),
            new Restaurant("Restaurant3", "Description3"),
            new Restaurant("Restaurant4", "Description4"));


    @Mock
    private RestaurantRepository mRestaurantRepository;

    @Mock
    private RestaurantContract.View mRestaurantView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<RestaurantRepository.LoadRestaurantsCallback> mLoadRestaurantCallbackCaptor;

    private RestaurantPresenter mNotesPresenter;

    @Before
    public void setupRestaurantsPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mNotesPresenter = new RestaurantPresenter(mRestaurantRepository, mRestaurantView);
    }

    @Test
    public void loadRestaurantsFromRepositoryAndLoadIntoView() {
        // Given an initialized NotesPresenter with initialized notes
        // When loading of Notes is requested
        mNotesPresenter.loadRestaurants(true);

        // Callback is captured and invoked with stubbed notes
        verify(mRestaurantRepository).getRestaurants(mLoadRestaurantCallbackCaptor.capture());
        mLoadRestaurantCallbackCaptor.getValue().onRestaurantsLoaded(RESTAURANTS);

        // Then progress indicator is hidden and notes are shown in UI
        verify(mRestaurantView).setProgressIndicator(false);
        verify(mRestaurantView).showRestaurants(RESTAURANTS);
    }

    @Test
    public void clickOnRestaurant_ShowsDetailUi() {
        // Given a stubbed note
        Restaurant requestedNote = new Restaurant("Details Requested", "For this note");

        // When open note details is requested
        mNotesPresenter.openRestaurantDetails(requestedNote);

        // Then note detail UI is shown
        verify(mRestaurantView).showRestaurantDetailUi(any(String.class));
    }
}
