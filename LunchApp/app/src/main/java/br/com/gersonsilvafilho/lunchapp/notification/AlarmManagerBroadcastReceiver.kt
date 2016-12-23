package br.com.gersonsilvafilho.lunchapp.notification

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.NotificationCompat
import br.com.gersonsilvafilho.lunchapp.R
import br.com.gersonsilvafilho.lunchapp.restaurants.RestaurantsActivity
import java.util.*

class AlarmManagerBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val mBuilder = NotificationCompat.Builder(context)
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
        mBuilder.setContentTitle("Lunch place was selected!")
        mBuilder.setContentText("Click here to see today voting")
        val resultIntent = Intent(context, RestaurantsActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(RestaurantsActivity::class.java)

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(resultPendingIntent)

        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build())

    }

    fun CancelAlarm(context: Context) {
        val intent = Intent(context, AlarmManagerBroadcastReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

    fun setTimer(context: Context) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmManagerBroadcastReceiver::class.java)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 13)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pi = PendingIntent.getBroadcast(context, 0, intent, 0)
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                (1000 * 30).toLong(), pi)
    }
}