package com.gracedev.expensetracker.model

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

    @Query("UPDATE expense SET name = :name, nominal = :nominal, date = :date, budget_id = :budgetId WHERE uuid = :id")
    fun update(name: String, nominal: Int, date: String, budgetId: Int, id: Int)

    @Update
    fun updateExpense(expense: Expense)

    @Query("SELECT * FROM expense")
    fun selectAllExpense(): List<Expense>

    @Query("SELECT * FROM expense WHERE uuid = :id")
    fun selectExpense(id: Int): Expense

    @Query("SELECT * FROM expense WHERE budget_id = :budgetId")
    fun selectExpenseByBudgetId(budgetId: Int): List<Expense>

    @Query("SELECT SUM(nominal) FROM expense WHERE budget_id = :budgetId")
    fun getTotalExpenseByBudgetId(budgetId: Int): Int?

    @Delete
    fun deleteExpense(expense: Expense)
}
