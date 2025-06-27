package com.gracedev.expensetracker.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gracedev.expensetracker.databinding.ReportItemLayoutBinding
import com.gracedev.expensetracker.model.Budget
import java.text.NumberFormat
import java.util.Locale

class ReportListAdapter(
    private val budgetList: List<Budget>,
    private val usedBudgetMap: Map<Int, Int> // key: budgetId, value: total expense
) : RecyclerView.Adapter<ReportListAdapter.ReportViewHolder>() {

    private val formatter = NumberFormat.getNumberInstance(Locale("in", "ID"))

    class ReportViewHolder(val binding: ReportItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding = ReportItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ReportViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return budgetList.size
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val budget = budgetList[position]
        val used = usedBudgetMap[budget.uuid] ?: 0
        val max = budget.budget
        val left = max - used

        with(holder.binding) {
            txtBudgetName.text = budget.name
            txtTotalBudget.text = "Total: IDR ${formatter.format(max)}"
            txtUsedBudget.text = "Used: IDR ${formatter.format(used)}"
            txtBudgetLeft.text = "Budget Left: IDR ${formatter.format(left)}"
            progressBarBudget.max = max
            progressBarBudget.progress = used
        }
    }
}

