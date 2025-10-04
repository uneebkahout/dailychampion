package com.lsp.dailchampion.data.Local.Task

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.lsp.dailchampion.Presentaion.ViewModel.TaskList
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

 @Query("SELECT * FROM task_table where date=:date and taskPriority ='Home Screen' ")
 fun getHomeScreenTask(date:String): Flow<List<Task>>;

 @Query("UPDATE task_table SET isCompleted = 1 where id in  (:id)")
suspend  fun completeTask(id: List<Int>);


 @Query("SELECT * FROM task_table where date =:date AND isCompleted  = 1")
 fun  getCompletedTas(date: String): Flow<List<TaskList>>
}









