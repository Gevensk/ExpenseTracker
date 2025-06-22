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

class ListBudgetViewModel (application: Application)
    : AndroidViewModel(application), CoroutineScope {
    val budgetLD = MutableLiveData<List<Budget>>()
    val budgetLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    fun refresh() {
        loadingLD.value = true
        budgetLoadErrorLD.value = false
        launch {
            val db = buildDb(getApplication())
            budgetLD.postValue(db.budgetDao().selectAllBudget())
            loadingLD.postValue(false)
        }
    }

    fun clearTask(budget: Budget) {
        launch {
            val db = BudgetDatabase.buildDatabase(
                getApplication()
            )
            db.budgetDao().deleteBudget(budget)

            budgetLD.postValue(db.budgetDao().selectAllBudget())
        }
    }


}