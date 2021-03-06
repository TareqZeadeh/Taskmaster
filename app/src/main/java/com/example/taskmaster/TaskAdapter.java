package com.example.taskmaster;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    List<com.amplifyframework.datastore.generated.model.Task> tasks;


    public TaskAdapter(List<com.amplifyframework.datastore.generated.model.Task> tasks) {
        this.tasks = tasks;

    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task,parent,false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.task = tasks.get(position);
        TextView textView = holder.itemView.findViewById(R.id.textViewfrag);
            textView.setText(holder.task.getTitle());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public Task task;
        View itemView;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent= new Intent(view.getContext(),taskDetail.class);
            intent.putExtra("title",task.getTitle());
            intent.putExtra("body",task.getBody());
            intent.putExtra("state",task.getState());
            intent.putExtra("fileKey",task.getFileKey());
            intent.putExtra("lat",task.getLat());
            intent.putExtra("lon",task.getLon());
            view.getContext().startActivity(intent);
        }
    }
}
