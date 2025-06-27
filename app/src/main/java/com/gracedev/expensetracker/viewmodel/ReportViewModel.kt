package com.gracedev.expensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.gracedev.expensetracker.model.Budget
import com.gracedev.expensetracker.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ReportViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    val budgetListLD = MutableLiveData<List<Budget>>()
    val usedBudgetMapLD = MutableLiveData<Map<Int, Int>>()
    val totalExpense = MutableLiveData<Int>()
    val totalBudget = MutableLiveData<Int>()

    fun refresh() {
        launch {
            val db = buildDb(getApplication())
            val budgets = db.budgetDao().selectAllBudget()

            val usedMap = mutableMapOf<Int, Int>()
            var totalUsed = 0
            var totalMax = 0

            for (budget in budgets) {
                // Safe cast result to Int if DAO return type isn't yet Int?
                val used = db.expenseDao().getTotalExpenseByBudgetId(budget.uuid) as? Int ?: 0
                usedMap[budget.uuid] = used
                totalUsed += used
                totalMax += budget.budget
            }

            budgetListLD.postValue(budgets)
            usedBudgetMapLD.postValue(usedMap)
            totalExpense.postValue(totalUsed)
            totalBudget.postValue(totalMax)
        }
    }
}
