package com.example.alarmclock;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlarmDataSource {

    private SQLiteDatabase database;
    private AlarmDbHelper dbHelper;

    public AlarmDataSource(Context context) {
        dbHelper = new AlarmDbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();

    }

    public void close() {
        dbHelper.close();
    }

    public long addAlarm(Alarm alarm) {
        ContentValues values = new ContentValues();
        values.put(AlarmDbHelper.COLUMN_HOUR, alarm.getHour());
        values.put(AlarmDbHelper.COLUMN_MINUTE, alarm.getMinute());
        values.put(AlarmDbHelper.COLUMN_STATE, alarm.getState() ? 1 : 0);
        return database.insert(AlarmDbHelper.TABLE_NAME, null, values);
    }

    public List<Alarm> getAllAlarms() {
        ArrayList<Alarm> alarms = new ArrayList<>(0);
        Cursor cursor = database.query(
                AlarmDbHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDbHelper.COLUMN_ID));
            int hour = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDbHelper.COLUMN_HOUR));
            int minute = cursor.getInt(cursor.getColumnIndexOrThrow(
                    AlarmDbHelper.COLUMN_MINUTE));
            boolean state = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDbHelper.COLUMN_STATE)) == 1;
            alarms.add(new Alarm(id, hour, minute, state));
        }
        cursor.close();
        return alarms;
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
    public void updateAlarm(Alarm alarm) {
        ContentValues values = new ContentValues();
        values.put(AlarmDbHelper.COLUMN_STATE, alarm.getState() ? 1 : 0);
        database.update(
                AlarmDbHelper.TABLE_NAME,
                values,
                AlarmDbHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(alarm.getId())} // Use the ID for updating
        );
    }
    public void deleteAlarm(Alarm alarm) {
        database.delete(
                AlarmDbHelper.TABLE_NAME,
                AlarmDbHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(alarm.getId())} // Use the ID for deleting
        );
    }
}
