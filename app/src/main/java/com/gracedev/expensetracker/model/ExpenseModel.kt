package com.gracedev.expensetracker.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Expense(
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "nominal")
    var nominal: Int,

    @ColumnInfo(name = "date")
    var date: Int,

    @ColumnInfo(name = "budget_id")
    var budgetId: Int,
    @ColumnInfo(name = "user_id")
    var userId: Int

) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}
