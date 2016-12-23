package br.com.gersonsilvafilho.lunchapp.restaurants

import android.content.Intent
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.NavigationView
import android.support.test.espresso.IdlingResource
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import br.com.gersonsilvafilho.lunchapp.R
import br.com.gersonsilvafilho.lunchapp.username.UsernameActivity
import br.com.gersonsilvafilho.lunchapp.util.EspressoIdlingResource
import br.com.gersonsilvafilho.lunchapp.util.UserInfo
import kotlinx.android.synthetic.main.activity_restaurants.*
import kotlinx.android.synthetic.main.app_bar_restaurants.*
import java.util.*

class RestaurantsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurants)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()

        //Initial top text
        getSupportActionBar()?.setTitle(getString(R.string.nav_today_text))

        nav_view.setNavigationItemSelectedListener(this)

        val username_textview = nav_view.getHeaderView(0).findViewById(R.id.nav_username) as TextView

        username_textview.setText(UserInfo.username)
        if (null == savedInstanceState) {
            initFragment(RestaurantsFragment.newInstance(Date()))
        }
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        var fragment: Fragment? = null
        if (id == R.id.nav_home) {
            toolbar.setTitle(getString(R.string.nav_today_text))
            fragment = RestaurantsFragment.newInstance(Date())
        } else if (id == R.id.nav_yesterday) {
            toolbar.setTitle(getString(R.string.nav_yesterday_text))
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -1)
            //This ensure that can't vote from yesterday tab
            cal.set(Calendar.HOUR_OF_DAY, 14)
            fragment = RestaurantsFragment.newInstance(cal.time)
        } else if (id == R.id.nav_tomorrow) {
            toolbar.setTitle(getString(R.string.nav_tomorrow_text))
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, 1)
            //This ensure that can vote in tomorrow tab
            cal.set(Calendar.HOUR_OF_DAY, 12)
            fragment = RestaurantsFragment.newInstance(cal.time)
        } else if (id == R.id.nav_username) {
            val intent = Intent(this, UsernameActivity::class.java)
            intent.putExtra(UsernameActivity.EXTRA_IS_NAME_CHANGE, true)
            startActivity(intent)
            return true
        } else if (id == R.id.nav_about) {

        }
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.contentFrame, fragment).commit()

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initFragment(restauratFragment: Fragment) {
        // Add the RestaurantsFragment to the layout
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.contentFrame, restauratFragment)
        transaction.commit()
    }

    @VisibleForTesting
    fun getCountingIdlingResource(): IdlingResource {
        return EspressoIdlingResource.idlingResource
    }
}
