package com.lsp.dailchampion.data.DI

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.lsp.dailchampion.data.Task.TaskDao
import com.lsp.dailchampion.data.Task.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HiltViewModel {
    @Provides
    fun provideDatabase(application: Application): TaskDatabase{
        return Room.databaseBuilder(
            application.baseContext,
            TaskDatabase :: class.java,
            "daily_champion"
        ).fallbackToDestructiveMigration().build()
    }
    @Provides
    fun providesTaskDao(db: TaskDatabase) : TaskDao{
    return  db.tasKDao();
    }
}