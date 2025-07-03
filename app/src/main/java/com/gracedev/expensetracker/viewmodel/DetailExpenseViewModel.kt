package com.gracedev.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
    val budgetNameLD = MutableLiveData<String>()

    fun getBudgetNameById(budgetId: Int, userId:Int): LiveData<String> {
        val db = buildDb(getApplication())
        return db.budgetDao().getBudgetNameById(budgetId, userId)
    }

    fun addExpense(list: List<Expense>) {
        launch {
            val db = buildDb(getApplication())
            db.expenseDao().insertAll(*list.toTypedArray())
        }
    }

    fun fetch(uuid: Int, userId:Int) {
        launch {
            val db = buildDb(getApplication())
            expenseLD.postValue(db.expenseDao().selectExpense(uuid, userId))
        }
    }

    fun update(name: String, nominal: Int, date: Int, budgetId: Int, uuid: Int, userId:Int) {
        launch {
            val db = buildDb(getApplication())
            db.expenseDao().update(name, nominal, date, budgetId, uuid, userId)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}
