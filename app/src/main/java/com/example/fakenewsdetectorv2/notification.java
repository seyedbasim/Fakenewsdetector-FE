package com.example.fakenewsdetectorv2;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class notification extends Application {
    public static final String CHANNEL_1_ID ="Channel1ID";

    @Override
    public void onCreate() {
        super.onCreate();
        createNoticationChannel();
    }

    private void createNoticationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel Channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel1ID",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            Channel1.setDescription("This is channel 1");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(Channel1);
        }

    }
}
