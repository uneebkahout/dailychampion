package com.lsp.dailchampion.data.Repository

import com.lsp.dailchampion.ViewModel.TaskList
import com.lsp.dailchampion.data.Task.Task
import com.lsp.dailchampion.data.Task.TaskDao
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
            entities.map { TaskList(it.id,it.title, it.date, it.description, it.isCompleted) }
        }
    }

    fun getTaskByDate(date:String): Flow<List<TaskList>>{
        return taskDao.getTaskByDate(date = date).map { entites->

            entites.map { TaskList(it.id,it.title,it.date,it.description, it.isCompleted) }
        }
    }

    suspend fun  deleteTask(task: Task): Int{
        return taskDao.deleteTask(task = task);
    }
}