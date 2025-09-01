package com.lsp.dailchampion.ViewModel

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.util.copy
import com.lsp.dailchampion.data.Repository.TaskRepository
import com.lsp.dailchampion.data.Task.Task
import com.lsp.dailchampion.data.Task.TaskDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.ref.Cleaner

@HiltViewModel
class MyViewModel @Inject constructor(
    private  val taskRepository: TaskRepository
) : ViewModel() {
private  val _taskState = MutableStateFlow(TaskDataState())
    val taskState = _taskState.asStateFlow()
    private  val _state = MutableStateFlow(ViewModelState())
    val state = _state.asStateFlow()


    val taskList: StateFlow<List<TaskList>> = taskRepository.getTask()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val taskListByDate : StateFlow<List<TaskList>> = taskState.map { it.todayDate }
        .distinctUntilChanged()
        .flatMapLatest {date->
            taskRepository.getTaskByDate(date = date)
        }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getGreetingMessage() :String{
        val hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return  when(hours){
            in 5 .. 11  -> "Good Morning"
            in 12 .. 16 -> "Good AfterNoon"
            in 17 .. 20 -> "Good Evening"
            else -> "Good Night"
        }
    }

    fun updateGreetingMessage(){
        _state.update {it->
            it.copy(
                greetingMessage = getGreetingMessage()
            )

        }
    }


    fun updateTodayDate(date:String){
        _taskState.update { it->
            it.copy(
                todayDate = date
            )
        }
    }

    fun updateTitle(title:String){
        _taskState.update { it->
            it.copy(
                title =title
            )
        }
    }

    fun updateDescription(description :String){
        _taskState.update { it->
            it.copy(
                description =description
            )
        }
    }

    fun toggleShowDatePicker(){
        _taskState.update {it->
            it.copy(
                showDatePicker = !it.showDatePicker
            )

        }
    }

    fun toggleLoading(){
        _taskState.update { it->
            it.copy(
                loading = !it.loading
            )
        }

    }
    fun createTask(){

        viewModelScope.launch {
            toggleLoading()
            try {
            val task = Task(title = taskState.value.title, date = taskState.value.todayDate, description = taskState.value.description, isCompleted = false)
               val  response = taskRepository.createTask(task = task);
                if(response>0){
                    toggleLoading()
                    _taskState.update { it->
                        it.copy(
                            message = "Task Added Successfully"
                        )
                    }

                    clearState()
                }
                else{

                    toggleLoading()
                    _taskState.update { it->
                        it.copy(
                            message = "Error"
                        )
                    }
                }
            }catch (e: Exception){
                Log.d("TAG","Message: ${e.localizedMessage}}")
                toggleLoading()
            }
        }
    }


    fun updateTask(){
        viewModelScope.launch {
            toggleLoading()
            try {
            val task = Task(id =  taskState.value.id , title = taskState.value.title , description = taskState.value.description, date = taskState.value.todayDate, isCompleted = false);

                val response = taskRepository.createTask(task);
                if (response > 0){
                    toggleLoading()
                    _taskState.update { it->
                        it.copy(
                            message = "Task Updated Successfully"
                        )
                    }
                    delay(2000)
                    _taskState.update { it->
                        it.copy(
                            message = ""
                        )
                    }
                }else{

                    toggleLoading()
                    _taskState.update { it->
                        it.copy(
                            message = "Error"
                        )
                    }
                }
            }catch (e: Exception){}

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun deleteTask(){
        viewModelScope.launch {
            try {
                toggleLoading()
                val task = Task(id =  taskState.value.id , title = taskState.value.title , description = taskState.value.description, date = taskState.value.todayDate, isCompleted = false);
            val response = taskRepository.deleteTask(task);
                if (response>0){
                        toggleLoading();
                    clearState()

                }
                else{
                    toggleLoading();
                    _taskState.update { it->
                        it.copy(
                            message = "Error"
                        )
                    }
                }
            }catch (e: Exception){
                Log.d("TAG","${e.localizedMessage}")
            }
        }
    }


    fun updateID(id:Int){
        _taskState.update {it->
            it.copy(
                id = id
            )
        }
    }

    fun clearState(){
        _taskState.update { it->
            it.copy(
                title = "",
                description = "",

                message = "",
                showDatePicker = false
            )
        }
    }
}