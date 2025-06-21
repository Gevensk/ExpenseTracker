package com.gracedev.expensetracker.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg budget:Budget)

    @Query("UPDATE budget SET name=:name, budget=:budget WHERE uuid = :id")
        fun update(name:String, budget:Int, id:Int)

    @Update
    fun updateBudget(budget:Budget)

    @Query("SELECT * FROM budget")
    fun selectAllBudget(): List<Budget>

    @Query("SELECT * FROM budget WHERE uuid= :id")
    fun selectBudget(id:Int): Budget

    @Delete
    fun deleteBudget(budget:Budget)

}