package com.lsp.dailchampion.data.Task

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
@PrimaryKey(autoGenerate = true) var id:Int = 0,
    val title:String,
    val date : String,
    val description :String ,

)