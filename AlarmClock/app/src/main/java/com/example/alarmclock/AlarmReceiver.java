package com.example.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;
    private CustomAdapter adapter;

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmPlayerManager.startAlarm(context);


    }
}
