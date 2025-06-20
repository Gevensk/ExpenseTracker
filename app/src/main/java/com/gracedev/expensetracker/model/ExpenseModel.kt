package com.gracedev.expensetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "nominal")
    var nominal: Int,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "budget_id")
    var budgetId: Int
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}
