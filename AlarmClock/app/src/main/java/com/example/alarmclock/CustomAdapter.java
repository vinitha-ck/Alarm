package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.AlarmViewHolder> {

    private Context context; // Corrected variable name
    private AlarmDataSource dataSource;
    private List<Alarm> alarmList;
    private AlarmReceiver alarmReceiver;

    public CustomAdapter(Context context, List<Alarm> alarms, AlarmDataSource dataSource) {
        this.context = context;
        this.alarmList = alarms;
        this.dataSource = new AlarmDataSource(context);

    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new AlarmViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);
        String time = String.format(Locale.getDefault(), "%02d:%02d", alarm.getHour(), alarm.getMinute());
        String setTime=convertTo12HourFormat(time);
        holder.timeTextView.setText(setTime);
        holder.switchToggle.setChecked(alarm.getState());

        holder.switchToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.setState(isChecked);
                dataSource.updateAlarm(alarm);

                if (!isChecked) {
                    cancelAlarm(alarm);
                    AlarmPlayerManager.stopAlarm();
                }
                else {
                    setAlarm(alarm);
                }
            }
        });


        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm(alarm);
                if (dataSource != null) {
                    dataSource.deleteAlarm(alarm);
                }
                alarmList.remove(alarm);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }
    public String convertTo12HourFormat(String time24hr) {
        try {
            DateFormat inputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            DateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date date = inputFormat.parse(time24hr);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void setAlarm(Alarm alarm) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar = (Calendar) calendar1.clone();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(calendar1)) {
            calendar.add(Calendar.DATE, 1);
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        Toast.makeText(context, " Alarm set successfully", Toast.LENGTH_SHORT).show();
    }
    private void cancelAlarm(Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager)context. getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, "Alarm cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public void addAlarm(Alarm alarm) {
        alarmList.add(alarm);
        notifyItemInserted(alarmList.size() - 1);
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        Switch switchToggle;
        ImageView deleteIcon;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.textViewTime);
            switchToggle = itemView.findViewById(R.id.switchToggle);
            deleteIcon = itemView.findViewById(R.id.imageViewDelete);
        }
    }
}
