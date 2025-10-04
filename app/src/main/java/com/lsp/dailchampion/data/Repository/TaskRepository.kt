package com.lsp.dailchampion.data.Repository

import com.lsp.dailchampion.Presentaion.ViewModel.TaskList
import com.lsp.dailchampion.data.Local.Task.Task
import com.lsp.dailchampion.data.Local.Task.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    suspend fun createTask(task: Task): Long{
        return taskDao.upsertTask(task = task);
    }
    fun getTask(): Flow<List<TaskList>> {
        return taskDao.getTaskSortedByName().map { entities ->
            entities.map { TaskList(it.id,it.title, it.date, it.description,it.taskPriority ,it.isCompleted) }
        }
    }

    fun getTaskByDate(date:String): Flow<List<TaskList>>{
        return taskDao.getTaskByDate(date = date).map { entites->

            entites.map { TaskList(it.id,it.title,it.date,it.description, it.taskPriority,it.isCompleted) }
        }
    }
    fun getHomeScreenTask(date:String): Flow<List<TaskList>>{
        return  taskDao.getHomeScreenTask(date = date).map { entities->
            entities.map { TaskList(id = it.id, title = it.title, date = it.date, description = it.description, taskPriority = it.taskPriority, isCompleted = it.isCompleted) }
        }
    }

    suspend fun  deleteTask(task: Task): Int{
        return taskDao.deleteTask(task = task);
    }
    suspend fun  completeTask(id: List<Int>){
        return taskDao.completeTask(id = id);
    }
    suspend fun  getCompleteTask(date: String): Flow<List<TaskList>>{
        return taskDao.getCompletedTas(date = date);
    }
}