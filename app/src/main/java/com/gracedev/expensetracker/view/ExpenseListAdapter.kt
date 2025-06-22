package com.gracedev.expensetracker.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gracedev.expensetracker.databinding.ExpenseItemLayoutBinding
import com.gracedev.expensetracker.model.Expense

class ExpenseListAdapter(private val expenseList: ArrayList<Expense>,
                         private val budgetNameMap: Map<Int, String> = emptyMap()) :
    RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(val binding: ExpenseItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var budgetMap: Map<Int, String> = emptyMap()

    fun setBudgetMap(map: Map<Int, String>) {
        budgetMap = map
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ExpenseItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenseList[position]

        holder.binding.txtDate.text = expense.date
        holder.binding.textNominal.text = "IDR ${expense.nominal}"
        // Ambil nama budget dari map
        val budgetName = budgetMap[expense.budgetId] ?: "Unknown"
        holder.binding.chipBudget.text = budgetName
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    fun updateExpenseList(newExpenseList: List<Expense>) {
        expenseList.clear()
        expenseList.addAll(newExpenseList)
        notifyDataSetChanged()
    }
}
