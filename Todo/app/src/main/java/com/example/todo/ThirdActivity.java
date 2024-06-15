package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThirdActivity extends AppCompatActivity {

    private int hour, minute;
    private DataSource dataSource;
    private List<Task> taskList;
    private TaskAdapter adapter;
    private static final int TIME_PICKER_ID = 111; // unique ID for TimePicker dialog

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        String subtext = intent.getStringExtra("subtext");
        String time = intent.getStringExtra("time");
        final int position = intent.getIntExtra("position", -1);

        EditText editTextText = findViewById(R.id.editTextText);
        EditText editTextSubtext = findViewById(R.id.editTextSubtext);
        TextView displayTime = findViewById(R.id.displayTime);
        ImageView imageViewTimePicker = findViewById(R.id.imageViewTimePicker);

        editTextText.setText(text);
        editTextSubtext.setText(subtext);
        displayTime.setText(time);

        // Set click listener for the imageView
        imageViewTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize a TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(ThirdActivity.this,R.style.timepicker,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                                // Update the selected hour and minute
                                hour = hourOfDay;
                                minute = minuteOfDay;
                                // Update the text of the displayTime to show the selected time

                                String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                                String setTime=convertTo12HourFormat(time);

                                displayTime.setText(setTime);
                            }
                        }, hour, minute, true);

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        // Handle the save button click
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated values
                String updatedText = editTextText.getText().toString();
                String updatedSubtext = editTextSubtext.getText().toString();
                String selectedTime = displayTime.getText().toString();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("text", updatedText);
                resultIntent.putExtra("subtext", updatedSubtext);
                resultIntent.putExtra("time", selectedTime);
                resultIntent.putExtra("position", position);

                setResult(RESULT_OK, resultIntent);
                finish(); // Finish ThirdActivity and return to SecondActivity
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

}
