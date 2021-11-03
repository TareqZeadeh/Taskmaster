package com.example.taskmaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE id = :id")
    Task getTaskById(Long id);

    @Insert
    void insertTask(Task task);

}
