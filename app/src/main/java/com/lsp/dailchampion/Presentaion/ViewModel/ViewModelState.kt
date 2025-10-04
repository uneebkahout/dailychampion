package com.lsp.dailchampion.Presentaion.ViewModel

import android.icu.text.CaseMap
import androidx.compose.foundation.MutatePriority
import androidx.compose.ui.graphics.Color

data class ViewModelState (
    val greetingMessage :String = ""
    )

data class  TaskDataState(
    val title: String="",
    val description :String ="",
    val todayDate:String = "",
    val taskPriority: String = "",
    val message:String="",
    val id:Int = 0,

    val showDatePicker :Boolean = false,
    val loading: Boolean=false,
    val showDropDownMenu : Boolean =false,



    )
data class Feature(
    val name: String,
    val emoji: String,
    val color: Color,
    val motivation: String
)

data class TaskList(
    val id:Int, 
    val title:String,
    val date : String,
    val description :String,
    val taskPriority: String,

    val isCompleted:Boolean
)