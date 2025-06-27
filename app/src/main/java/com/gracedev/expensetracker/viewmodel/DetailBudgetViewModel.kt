package com.gracedev.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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
    val budgetLD = MutableLiveData<Budget>()
    val totalExpenseLD = MutableLiveData<Int>()

    fun addTodo(list: List<Budget>) {
        launch {
            val db =  buildDb(getApplication())
            db.budgetDao().insertAll(*list.toTypedArray())
        }
    }

    fun fetchTotalExpense(budgetId: Int) {
        launch {
            val db = buildDb(getApplication())
            val total = db.expenseDao().getTotalExpenseByBudgetIdDirect(budgetId) ?: 0
            totalExpenseLD.postValue(total)
        }
    }

    fun fetch(uuid:Int) {
        launch {
            val db = buildDb(getApplication())
            budgetLD.postValue(db.budgetDao().selectBudget(uuid))
        }
    }

    fun update(name:String, budget:Int, uuid:Int) {
        launch {
            val db = buildDb(getApplication())
            db.budgetDao().update(name, budget, uuid)
        }
    }


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

}