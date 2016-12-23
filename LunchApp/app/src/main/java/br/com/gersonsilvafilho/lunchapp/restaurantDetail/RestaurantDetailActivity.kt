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
import android.support.annotation.VisibleForTesting
import android.support.test.espresso.IdlingResource
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import br.com.gersonsilvafilho.lunchapp.R
import br.com.gersonsilvafilho.lunchapp.util.EspressoIdlingResource
import java.util.*

/**
 * Displays Restaurant details screen.
 */
class RestaurantDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        getSupportActionBar()?.setTitle(getString(R.string.restaurant_details_topbar))

        // Get the requested Restaurant id
        val RestaurantId = intent.getStringExtra(EXTRA_RESTAURANT_ID)
        val dateUnix = intent.getLongExtra(EXTRA_DATE, 0)

        initFragment(RestaurantDetailFragment.newInstance(RestaurantId, Date(dateUnix)))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initFragment(detailFragment: Fragment) {
        // Add the RestaurantsDetailFragment to the layout
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.contentFrame, detailFragment)
        transaction.commit()
    }

    val countingIdlingResource: IdlingResource
        @VisibleForTesting
        get() = EspressoIdlingResource.idlingResource

    companion object {

        val EXTRA_RESTAURANT_ID = "Restaurant_ID"
        val EXTRA_DATE = "date"
    }
}
