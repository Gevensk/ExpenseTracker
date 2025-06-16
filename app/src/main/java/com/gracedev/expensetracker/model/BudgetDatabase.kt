package com.gracedev.expensetracker.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gracedev.expensetracker.util.DB_NAME

@Database(entities = arrayOf(Budget::class), version =  2)
abstract class BudgetDatabase: RoomDatabase() {
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile private var instance: BudgetDatabase ?= null
        private val LOCK = Any()

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                BudgetDatabase::class.java,
                DB_NAME
                ).build()
        operator fun invoke(context:Context) {
            if(instance == null) {
                synchronized(LOCK) {
                    instance ?: buildDatabase(context).also {
                        instance = it
                    }
                }
            }
        }


    }
}
