package com.mainway.weatherforecast.Presenter.Main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mainway.weatherforecast.R;
import com.mainway.weatherforecast.View.Main.MainActivity;

public class DateService extends Service {

    private final String TAG = getClass().getName();
    private Boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    public DateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }


    public Runnable myTask=new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "THE BACKGROUND SERVICE IS RUNNING");
            createNotification();

            stopSelf();
        }
    };

    public void createNotification() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification notification = new Notification.Builder(this, "myapp")
                    .setContentTitle("Weather Forecast")
                    .setContentText("On this date you should watch the weather")
                    .setSmallIcon(R.drawable.ic_baseline_air_24)
                    .setContentIntent(pendingIntent)
                    .build();

            Log.i(TAG, "createNotification: ");
            manager.notify(1001, notification);
        }

    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }
}