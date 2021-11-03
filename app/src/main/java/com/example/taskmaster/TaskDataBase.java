package com.example.taskmaster;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class} ,version = 1)
public abstract class TaskDataBase extends RoomDatabase {
    public abstract TaskDAO taskDAO();

    public static TaskDataBase taskDataBase;

    public TaskDataBase() {
    }

    public static synchronized TaskDataBase getInstance(Context context){
        if(taskDataBase == null){
            taskDataBase = Room.databaseBuilder(context,
                    TaskDataBase.class,"TaskDataBase").allowMainThreadQueries().build();
        }
        return taskDataBase;
    }
}
