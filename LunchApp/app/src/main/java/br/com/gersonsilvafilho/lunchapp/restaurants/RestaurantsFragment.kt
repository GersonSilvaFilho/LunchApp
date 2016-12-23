package br.com.gersonsilvafilho.lunchapp.restaurants

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gersonsilvafilho.lunchapp.Injection
import br.com.gersonsilvafilho.lunchapp.R
import br.com.gersonsilvafilho.lunchapp.data.Restaurant
import br.com.gersonsilvafilho.lunchapp.restaurantDetail.RestaurantDetailActivity
import kotlinx.android.synthetic.main.fragment_restaurants.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RestaurantsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RestaurantsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestaurantsFragment : Fragment(), RestaurantContract.View {


    private var mActionsListener: RestaurantContract.UserActionsListener? = null

    private var mListAdapter: RestaurantAdapter? = null

    fun RestaurantsFragment() {
        // Requires empty public constructor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListAdapter = RestaurantAdapter(ArrayList<Restaurant>(0), mItemListener)
        mActionsListener = RestaurantPresenter(Injection.provideRestaurantsRepository(this.context), this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.fragment_restaurants, container, false)
        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        restaurant_list.adapter = mListAdapter

        val numColumns = 1

        restaurant_list.setHasFixedSize(true)
        restaurant_list.layoutManager = GridLayoutManager(context, numColumns)


        // Pull-to-refresh
        refresh_layout.setColorSchemeColors(
                ContextCompat.getColor(activity, R.color.colorPrimary),
                ContextCompat.getColor(activity, R.color.colorAccent),
                ContextCompat.getColor(activity, R.color.colorPrimaryDark))
        refresh_layout.setOnRefreshListener { mActionsListener?.loadRestaurants(true) }
    }

    companion object {
        fun newInstance(): RestaurantsFragment {
            return RestaurantsFragment()
        }
    }

    internal var mItemListener: RestaurantAdapter.RestaurantItemListener = object : RestaurantAdapter.RestaurantItemListener {
        override fun onRestaurantClick(clickedRestaurant: Restaurant) {
            mActionsListener?.openRestaurantDetails(clickedRestaurant)
        }
    }

    override fun setProgressIndicator(active: Boolean) {
        if (view == null) {
            return
        }

        // Make sure setRefreshing() is called after the layout is done with everything else.
        refresh_layout.post { refresh_layout.isRefreshing = active }
    }

    override fun showRestaurants(restaurants: List<Restaurant>) {
        mListAdapter?.replaceData(restaurants)
    }

    override fun showRestaurantDetailUi(RestaurantId: String) {
        val intent = Intent(context, RestaurantDetailActivity::class.java)
        intent.putExtra(RestaurantDetailActivity.EXTRA_RESTAURANT_ID, RestaurantId)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        mActionsListener?.loadRestaurants(true)
        showSnackbarText("Long click to add a Vote")
    }

    override fun showSnackbarText(textToShow: String) {
        Snackbar.make(this.view!!, textToShow, Snackbar.LENGTH_LONG).show()
    }
}
