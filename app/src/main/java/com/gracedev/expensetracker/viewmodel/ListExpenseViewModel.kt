package com.gracedev.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gracedev.expensetracker.model.Expense
import com.gracedev.expensetracker.model.BudgetDatabase
import com.gracedev.expensetracker.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListExpenseViewModel(application: Application)
    : AndroidViewModel(application), CoroutineScope {

    val expenseLD = MutableLiveData<List<Expense>>()
    val expenseLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun refresh() {
        loadingLD.value = true
        expenseLoadErrorLD.value = false
        launch {
            val db = buildDb(getApplication())
            expenseLD.postValue(db.expenseDao().selectAllExpenseOrdered())
            loadingLD.postValue(false)
        }
    }

    fun getTotalExpenseByBudgetId(budgetId: Int) =
        buildDb(getApplication()).expenseDao().getTotalExpenseByBudgetId(budgetId)


    fun refreshByBudget(budgetId: Int) {
        loadingLD.value = true
        expenseLoadErrorLD.value = false
        launch {
            val db = buildDb(getApplication())
            expenseLD.postValue(db.expenseDao().selectExpenseByBudgetId(budgetId))
            loadingLD.postValue(false)
        }
    }

    fun clearExpense(expense: Expense) {
        launch {
            val db = BudgetDatabase.buildDatabase(getApplication())
            db.expenseDao().deleteExpense(expense)
            expenseLD.postValue(db.expenseDao().selectAllExpenseOrdered())
        }
    }
}
