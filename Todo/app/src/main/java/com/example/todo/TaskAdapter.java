package com.example.todo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private DataSource dataSource;
    private List<Task> taskList;

    public TaskAdapter(Context context, List<Task> taskList, DataSource dataSource) {
        this.context = context;
        this.taskList = taskList;
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.textViewText.setText(task.getText());
        holder.textViewSubtext.setText(task.getSubtext());
        holder.textViewTime.setText(task.getTime());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isChecked());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("TaskAdapter", "Checkbox state changed for task ID: " + task.getId() + " to: " + isChecked);

            task.setChecked(isChecked);
            dataSource.updateTask(task.getId(), task.getText(), task.getSubtext(), task.getTime(), task.isChecked());
            notifyItemChanged(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void addTask(Task task) {
        taskList.add(task);
        notifyItemInserted(taskList.size() - 1);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewText;
        TextView textViewSubtext;
        TextView textViewTime;
        CheckBox checkBox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewText = itemView.findViewById(R.id.textViewTitle);
            textViewSubtext = itemView.findViewById(R.id.textViewSubTitle);
            textViewTime = itemView.findViewById(R.id.textViewDate);
            checkBox = itemView.findViewById(R.id.checkIcon);
        }
    }
}
