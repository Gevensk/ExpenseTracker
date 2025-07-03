package com.gracedev.expensetracker.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg expense: Expense)

    @Query("UPDATE expense SET name = :name, nominal = :nominal, date = :date, budget_id = :budgetId WHERE uuid = :id AND user_id = :userId")
    fun update(name: String, nominal: Int, date: Int, budgetId: Int, id: Int, userId: Int)

    @Update
    fun updateExpense(expense: Expense)

    @Query("SELECT * FROM expense WHERE user_id = :userId")
    fun selectAllExpense(userId: Int): List<Expense>

    @Query("SELECT * FROM expense WHERE user_id = :userId ORDER BY date DESC")
    fun selectAllExpenseOrdered(userId: Int): List<Expense>

    @Query("SELECT * FROM expense WHERE uuid = :id AND user_id = :userId")
    fun selectExpense(id: Int, userId: Int): Expense

    @Query("SELECT * FROM expense WHERE budget_id = :budgetId AND user_id = :userId")
    fun selectExpenseByBudgetId(budgetId: Int, userId: Int): List<Expense>

    @Query("SELECT SUM(nominal) FROM expense WHERE budget_id = :budgetId AND user_id = :userId")
    fun getTotalExpenseByBudgetId(budgetId: Int, userId: Int): LiveData<Int?>

    @Query("SELECT SUM(nominal) FROM expense WHERE budget_id = :budgetId AND user_id = :userId")
    fun getTotalExpenseByBudgetIdDirect(budgetId: Int, userId: Int): Int?

    @Delete
    fun deleteExpense(expense: Expense)
}

