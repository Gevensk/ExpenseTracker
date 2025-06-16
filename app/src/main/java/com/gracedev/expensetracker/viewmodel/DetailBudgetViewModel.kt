package com.gracedev.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gracedev.expensetracker.model.Budget
import com.gracedev.expensetracker.model.BudgetDatabase
import com.gracedev.expensetracker.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailBudgetViewModel (application: Application)
    : AndroidViewModel(application), CoroutineScope {
    private val job = Job()


    fun addTodo(list: List<Budget>) {
        launch {
            val db = buildDb(getApplication())
            db.budgetDao().insertAll(*list.toTypedArray())
        }
    }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

}