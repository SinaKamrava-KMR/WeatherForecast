package com.mainway.weatherforecast.Presenter;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel channel= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            channel = new NotificationChannel("myapp","alarm Manager Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("this notification has a alarm for special date ");
            notificationManager.createNotificationChannel(channel);

        }
    }
}
