package br.com.gersonsilvafilho.lunchapp.restaurants

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.gersonsilvafilho.lunchapp.R
import br.com.gersonsilvafilho.lunchapp.data.Restaurant

/**
 * The adapter used on Recycler View on RestaurantsFragment
 */
class RestaurantAdapter(restaurants: List<Restaurant>, private val mItemListener: RestaurantAdapter.RestaurantItemListener) : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    private var mRestaurants: List<Restaurant>? = null

    init {
        setList(restaurants)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val RestaurantView = inflater.inflate(R.layout.item_restaurant, parent, false)

        return ViewHolder(RestaurantView, mItemListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val restaurant = mRestaurants!![position]

        viewHolder.title.setText(restaurant.title);
        viewHolder.votes.setText(restaurant.votes.toString());
    }

    fun replaceData(restaurants: List<Restaurant>) {
        setList(restaurants)
        notifyDataSetChanged()
    }

    private fun setList(restaurants: List<Restaurant>) {
        mRestaurants = restaurants
    }

    override fun getItemCount(): Int {
        return mRestaurants!!.size
    }

    fun getItem(position: Int): Restaurant {
        return mRestaurants!![position]
    }

    inner class ViewHolder(itemView: View, private val mItemListener: RestaurantItemListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var title: TextView

        var votes: TextView

        init {
            title = itemView.findViewById(R.id.restaurant_detail_title) as TextView
            votes = itemView.findViewById(R.id.restaurant_votes) as TextView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = adapterPosition
            val Restaurant = getItem(position)
            mItemListener.onRestaurantClick(Restaurant)

        }
    }

    interface RestaurantItemListener {

        fun onRestaurantClick(clickedRestaurant: Restaurant)
    }
}