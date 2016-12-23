package br.com.gersonsilvafilho.lunchapp.notification;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import br.com.gersonsilvafilho.lunchapp.restaurants.RestaurantActivity;

public class MyAlarmReceiver extends BroadcastReceiver
{
    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr = null;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;


    @Override
    public void onReceive(Context context, Intent inten)
    {
       mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent newIntent = new Intent(context, RestaurantActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // If you use intent extras, remember to call PendingIntent.getActivity() with the flag
        // PendingIntent.FLAG_UPDATE_CURRENT, otherwise the same extras will be reused for every
        // notification.
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, newIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(context.getApplicationInfo().icon)
                        .setContentTitle("Lunch Place Selected!")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Lunch place selected!"))
                        .setContentText("Today we are lunching in ...");

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(0, mBuilder.build());
    }
}