package com.gracedev.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gracedev.expensetracker.model.Expense
import com.gracedev.expensetracker.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailExpenseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    val expenseLD = MutableLiveData<Expense>()

    fun addExpense(list: List<Expense>) {
        launch {
            val db = buildDb(getApplication())
            db.expenseDao().insertAll(*list.toTypedArray())
        }
    }

    fun fetch(uuid: Int) {
        launch {
            val db = buildDb(getApplication())
            expenseLD.postValue(db.expenseDao().selectExpense(uuid))
        }
    }

    fun update(name: String, nominal: Int, date: String, budgetId: Int, uuid: Int) {
        launch {
            val db = buildDb(getApplication())
            db.expenseDao().update(name, nominal, date, budgetId, uuid)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}
