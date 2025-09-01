package com.lsp.dailchampion.data.Task

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
@Upsert
suspend  fun upsertTask(task : Task): Long
@Delete
suspend  fun deleteTask(task :Task) : Int


@Query("SELECT * FROM task_table ORDER BY date ASC")

 fun getTaskSortedByName(): Flow<List<Task>>

 @Query("SELECT * FROM task_table WHERE date =:date And isCompleted = 0")
 fun getTaskByDate(date: String):Flow<List<Task>>
}









