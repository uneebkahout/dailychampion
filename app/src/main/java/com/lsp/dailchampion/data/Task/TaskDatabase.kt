package com.lsp.dailchampion.data.Task

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [Task ::class], version = 1 , exportSchema = true)
abstract  class  TaskDatabase : RoomDatabase(){
    abstract  fun tasKDao(): TaskDao

}