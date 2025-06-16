package com.gracedev.expensetracker.util

import android.content.Context
import com.gracedev.expensetracker.model.BudgetDatabase

val DB_NAME = "newbudgetdb"
fun buildDb(context: Context): BudgetDatabase {
    val db = BudgetDatabase.buildDatabase(context)
    return db
}
