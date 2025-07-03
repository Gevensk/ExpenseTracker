package com.gracedev.expensetracker.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg budget: Budget)

    @Query("UPDATE budget SET name = :name, budget = :budget WHERE uuid = :uuid AND user_id = :userId")
    fun update(name: String, budget: Int, uuid: Int, userId: Int)

    @Update
    fun updateBudget(budget: Budget)

    @Query("SELECT * FROM budget WHERE user_id = :userId")
    fun selectAllBudget(userId: Int): List<Budget>

    @Query("SELECT name FROM budget WHERE uuid = :budgetId AND user_id = :userId")
    fun getBudgetNameById(budgetId: Int, userId: Int): LiveData<String>

    @Query("SELECT * FROM budget WHERE uuid = :id AND user_id = :userId")
    fun selectBudget(id: Int, userId: Int): Budget?

    @Delete
    fun deleteBudget(budget: Budget)
}

