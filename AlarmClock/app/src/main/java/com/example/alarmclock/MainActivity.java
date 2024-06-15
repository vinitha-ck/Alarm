package com.example.alarmclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SECOND_ACTIVITY = 100;

    RecyclerView recyclerView;
    CustomAdapter adapter;
    AlarmDataSource dataSource;
    private List<Alarm> alarms ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new AlarmDataSource(this);
        alarms = dataSource.getAllAlarms();

        adapter = new CustomAdapter(this, alarms,dataSource);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button btnSetAlarm = findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        for (Alarm alarm : alarms) {
            if (alarm.getState()) {
                setAlarm(alarm);
            }
        }
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        Alarm alarm = new Alarm(hourOfDay, minute, true);
                        long id = dataSource.addAlarm(alarm);
                        alarm.setId((int) id);
                        adapter.addAlarm(alarm);
                        adapter.notifyDataSetChanged();

                        setAlarm(alarm);
                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }

    private void setAlarm(Alarm alarm) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarm.getId(), alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        Toast.makeText(this, " Alarm set successfully", Toast.LENGTH_SHORT).show();
    }
    private void cancelAlarm(Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarm.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show();
    }

}
