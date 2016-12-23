package br.com.gersonsilvafilho.lunchapp.username

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.gersonsilvafilho.lunchapp.R
import br.com.gersonsilvafilho.lunchapp.notification.AlarmManagerBroadcastReceiver
import br.com.gersonsilvafilho.lunchapp.restaurants.RestaurantsActivity
import br.com.gersonsilvafilho.lunchapp.restaurants.UsernameContract
import br.com.gersonsilvafilho.lunchapp.util.UserInfo
import kotlinx.android.synthetic.main.activity_username.*

class UsernameActivity : AppCompatActivity(), UsernameContract.View {

    private var  mActionsListener: UsernamePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)

        getSupportActionBar()?.setTitle(getString(R.string.username_toolbar_text))

        //Check if username has been set
        val forceChange = intent.getBooleanExtra(UsernameActivity.EXTRA_IS_NAME_CHANGE, false)
        if(UserInfo.username != "" && !forceChange)
        {
            showRestaurantlUi()
            return
        }

        mActionsListener = UsernamePresenter(this)

        //Set enter button click
        email_sign_in_button.setOnClickListener {

            //Set timer to notify every day at 13 PM
            setTimer()

            //Add the username to shared prefs
            UserInfo.username = username_textview.text.toString()

            //Start main activity
            showRestaurantlUi()
        }
    }

    override fun setUsername(username: String) {
        username_textview.setText(username)
    }

    override fun showRestaurantlUi() {
        val intent = Intent(this, RestaurantsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        this.overridePendingTransition(0, 0)
    }

    companion object {
        val EXTRA_IS_NAME_CHANGE = "Restaurant_ID"
    }

    private fun setTimer()
    {
        var alarm = AlarmManagerBroadcastReceiver()
        alarm.setTimer(this)
    }
}
