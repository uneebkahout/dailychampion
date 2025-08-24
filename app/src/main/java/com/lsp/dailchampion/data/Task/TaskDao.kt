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
suspend  fun deleteTask(task :Task)


@Query("SELECT * FROM task_table ORDER BY title ASC")

 fun getTaskSortedByName(): Flow<List<Task>>

}









