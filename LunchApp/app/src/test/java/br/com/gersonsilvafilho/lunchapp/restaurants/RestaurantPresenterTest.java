package br.com.gersonsilvafilho.lunchapp.restaurants;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import br.com.gersonsilvafilho.lunchapp.data.Restaurant;
import br.com.gersonsilvafilho.lunchapp.data.RestaurantRepository;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;


public class RestaurantPresenterTest {

    private static List<Restaurant> RESTAURANTS = Lists.newArrayList(
            new Restaurant(UUID.randomUUID().toString(), "Restaurant1", "Description1"),
            new Restaurant(UUID.randomUUID().toString(), "Restaurant2", "Description2"),
            new Restaurant(UUID.randomUUID().toString(), "Restaurant3", "Description3"),
            new Restaurant(UUID.randomUUID().toString(), "Restaurant4", "Description4"));

    public static final Date DATE = new Date();

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
        mNotesPresenter.loadRestaurants(DATE,true);

        // Callback is captured and invoked with stubbed notes
        verify(mRestaurantRepository).getRestaurants(eq(DATE), mLoadRestaurantCallbackCaptor.capture());
        mLoadRestaurantCallbackCaptor.getValue().onRestaurantsLoaded(RESTAURANTS);

        // Then progress indicator is hidden and notes are shown in UI
        verify(mRestaurantView).setProgressIndicator(false);
        verify(mRestaurantView).showRestaurants(RESTAURANTS);
    }

    @Test
    public void clickOnRestaurant_ShowsDetailUi() {
        // Given a stubbed note
        Restaurant requestedNote = new Restaurant(UUID.randomUUID().toString(), "Details Requested", "For this note");

        // When open note details is requested
        mNotesPresenter.openRestaurantDetails(requestedNote);

        // Then note detail UI is shown
        verify(mRestaurantView).showRestaurantDetailUi(any(String.class));
    }
}
