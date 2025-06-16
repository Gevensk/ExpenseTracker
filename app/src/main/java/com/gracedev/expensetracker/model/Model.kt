package com.gracedev.expensetracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Budget(
    @ColumnInfo(name="name")
    var name:String,
    @ColumnInfo(name="budget")
    var budget:Int
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0

}
