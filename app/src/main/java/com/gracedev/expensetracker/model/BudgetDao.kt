package com.gracedev.expensetracker.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg budget:Budget)

    @Query("SELECT * FROM budget")
    fun selectAllTodo(): List<Budget>

    @Query("SELECT * FROM budget WHERE uuid= :id")
    fun selectTodo(id:Int): Budget

    @Delete
    fun deleteTodo(todo:Budget)

}