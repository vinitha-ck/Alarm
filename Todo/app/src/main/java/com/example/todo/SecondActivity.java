package com.example.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private ArrayList<Task> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private DataSource dataSource;
    private static final int ADD_REQUEST_CODE = 1; // Request code for adding task
    private static final int EDIT_REQUEST_CODE = 2; // Request code for editing task

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        dataSource = new DataSource(this);
        dataList = (ArrayList<Task>) dataSource.getAllAlarms(); // Fetch all tasks from the database

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new TaskAdapter(this, dataList, dataSource); // Pass the DataSource to the adapter
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivityForResult(intent, ADD_REQUEST_CODE); // Request code for starting ThirdActivity
            }
        });
    }

    public void showPopupMenu(final View view) {
        final int position = recyclerView.getChildLayoutPosition((View) view.getParent());
        PopupMenu popupMenu = new PopupMenu(this, view);

        popupMenu.inflate(R.menu.popup_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_edit:
                        handleEditAction(position);
                        return true;
                    case R.id.menu_delete:
                        handleDeleteAction(position);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }

    private void handleEditAction(int position) {
        Task task = dataList.get(position);
        Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
        intent.putExtra("text", task.getText());
        intent.putExtra("subtext", task.getSubtext());
        intent.putExtra("time", task.getTime());
        intent.putExtra("isChecked", task.isChecked());
        intent.putExtra("position", position);
        intent.putExtra("id", task.getId());
        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }

    private void handleDeleteAction(int position) {
        Task task = dataList.get(position);
        dataSource.deleteTask(task);
        dataList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String text = data.getStringExtra("text");
            String subtext = data.getStringExtra("subtext");
            String time = data.getStringExtra("time");
            boolean isChecked = data.getBooleanExtra("isChecked", false);
            Task task = new Task(text, subtext, time, isChecked);
            long id = dataSource.addTask(task);
            if (id != -1) {
                task.setId((int) id);
                dataList.add(task);
                adapter.notifyItemInserted(dataList.size() - 1);
            }
        } else if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String editedText = data.getStringExtra("text");
            String editedSubtext = data.getStringExtra("subtext");
            String editedTime = data.getStringExtra("time");
            boolean isChecked = data.getBooleanExtra("isChecked", false);
            int position = data.getIntExtra("position", -1);
            int id = data.getIntExtra("id", -1);
            if (position != -1) {
                Task task = dataList.get(position);
                task.setText(editedText);
                task.setSubtext(editedSubtext);
                task.setTime(editedTime);
                task.setChecked(isChecked);
                dataSource.updateTask(id, editedText, editedSubtext, editedTime, isChecked);
                adapter.notifyItemChanged(position);
            }
        }
    }
}
