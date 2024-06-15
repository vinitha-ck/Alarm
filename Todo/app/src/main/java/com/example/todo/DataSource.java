package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private TaskDbHelper dbHelper;
    private SQLiteDatabase database;

    public DataSource(Context context) {
        dbHelper = new TaskDbHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskDbHelper.COLUMN_TEXT, task.getText());
        values.put(TaskDbHelper.COLUMN_SUBTEXT, task.getSubtext());
        values.put(TaskDbHelper.COLUMN_TIME, task.getTime());
        values.put(TaskDbHelper.COLUMN_STATE, task.isChecked());
        return database.insert(TaskDbHelper.TABLE_NAME, null, values);
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = database.query(
                TaskDbHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_ID));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_TEXT));
            String subtext = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_SUBTEXT));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_TIME));
            boolean state = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_STATE)) == 1;
            tasks.add(new Task(id, text, subtext, time, state));
        }
        cursor.close();
        return tasks;
    }

    public List<Task> getAllAlarms() {
        ArrayList<Task> tasks = new ArrayList<>(0);
        Cursor cursor = database.query(
                TaskDbHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_ID));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_TEXT));
            String subtext = cursor.getString(cursor.getColumnIndexOrThrow(
                    TaskDbHelper.COLUMN_SUBTEXT));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(
                    TaskDbHelper.COLUMN_TIME));
            boolean state = cursor.getInt(cursor.getColumnIndexOrThrow(TaskDbHelper.COLUMN_STATE)) == 1;
            tasks.add(new Task(id, text, subtext, time, state));
        }
        cursor.close();
        return tasks;
    }

    public long insertTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskDbHelper.COLUMN_TEXT, task.getText());
        values.put(TaskDbHelper.COLUMN_SUBTEXT, task.getSubtext());
        values.put(TaskDbHelper.COLUMN_TIME, task.getTime());
        values.put(TaskDbHelper.COLUMN_STATE, task.isChecked() ? 1 : 0);

        return database.insert(TaskDbHelper.TABLE_NAME, null, values);
    }

    public void deleteTask(Task task) {
        database.delete(
                TaskDbHelper.TABLE_NAME,
                TaskDbHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())} // Use the ID for deleting
        );
    }

    public void updateTask(long id, String text, String subtext, String time, boolean state) {
        ContentValues values = new ContentValues();
        values.put(TaskDbHelper.COLUMN_TEXT, text);
        values.put(TaskDbHelper.COLUMN_SUBTEXT, subtext);
        values.put(TaskDbHelper.COLUMN_TIME, time);
        values.put(TaskDbHelper.COLUMN_STATE, state ? 1 : 0);

        String selection = TaskDbHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        database.update(TaskDbHelper.TABLE_NAME, values, selection, selectionArgs);
    }
}
