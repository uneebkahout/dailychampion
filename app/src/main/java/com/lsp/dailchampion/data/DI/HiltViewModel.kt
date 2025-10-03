package com.lsp.dailchampion.data.DI

import android.app.Application
import androidx.room.Room
import com.lsp.dailchampion.data.Local.DaliChampionDatabase
import com.lsp.dailchampion.data.Local.Expense.ExpenseDao
import com.lsp.dailchampion.data.Local.Task.TaskDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HiltViewModel {
    @Provides
    fun provideDatabase(application: Application): DaliChampionDatabase{
        return Room.databaseBuilder(
            application.baseContext,
            DaliChampionDatabase :: class.java,
            "daily_champion"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideTaskDao(db: DaliChampionDatabase): TaskDao = db.tasKDao()

    @Provides
    fun provideExpenseDao(db: DaliChampionDatabase): ExpenseDao = db.expenseDao()
}