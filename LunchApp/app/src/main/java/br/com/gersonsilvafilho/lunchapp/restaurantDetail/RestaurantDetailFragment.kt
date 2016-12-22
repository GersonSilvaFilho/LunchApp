/*
 * Copyright (C) 2015 The Android Open Source Project
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

package br.com.gersonsilvafilho.lunchapp.restaurantDetail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.gersonsilvafilho.lunchapp.Injection
import br.com.gersonsilvafilho.lunchapp.R
import br.com.gersonsilvafilho.lunchapp.util.EspressoIdlingResource
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * Main UI for the Restaurant detail screen.
 */
class RestaurantDetailFragment : Fragment(), RestaurantDetailContract.View {

    private var mActionsListener: RestaurantDetailContract.UserActionsListener? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActionsListener = RestaurantDetailPresenter(Injection.provideRestaurantsRepository(), this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater!!.inflate(R.layout.fragment_detail, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()
        val RestaurantId = arguments.getString(ARGUMENT_Restaurant_ID)
        mActionsListener!!.openRestaurant(RestaurantId)
    }

    override fun setProgressIndicator(active: Boolean) {
        if (active) {
            restaurant_detail_title!!.text = ""
            restaurant_detail_description!!.text = getString(R.string.loading)
        }
    }

    override fun hideDescription() {
        restaurant_detail_description!!.visibility = View.GONE
    }

    override fun hideTitle() {
        restaurant_detail_title!!.visibility = View.GONE
    }

    override fun showDescription(description: String) {
        restaurant_detail_description!!.visibility = View.VISIBLE
        restaurant_detail_description!!.text = description
    }

    override fun showTitle(title: String) {
        restaurant_detail_title!!.visibility = View.VISIBLE
        restaurant_detail_title!!.text = title
    }

    override fun showImage(imageUrl: String) {
        // The image is loaded in a different thread so in order to UI-test this, an idling resource
        // is used to specify when the app is idle.
        EspressoIdlingResource.increment() // App is busy until further notice.

        restaurant_detail_image!!.visibility = View.VISIBLE

        // This app uses Glide for image loading
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(object : GlideDrawableImageViewTarget(restaurant_detail_image!!) {
                    override fun onResourceReady(resource: GlideDrawable,
                                                 animation: GlideAnimation<in GlideDrawable>?) {
                        super.onResourceReady(resource, animation)
                        EspressoIdlingResource.decrement() // App is idle.
                    }
                })
    }

    override fun hideImage() {
        restaurant_detail_image!!.setImageDrawable(null)
        restaurant_detail_image!!.visibility = View.GONE
    }

    override fun showMissingRestaurant() {
        restaurant_detail_title!!.text = ""
        restaurant_detail_description!!.text = getString(R.string.no_data)
    }

    companion object {

        val ARGUMENT_Restaurant_ID = "Restaurant_ID"

        fun newInstance(RestaurantId: String): RestaurantDetailFragment {
            val arguments = Bundle()
            arguments.putString(ARGUMENT_Restaurant_ID, RestaurantId)
            val fragment = RestaurantDetailFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
}
