package com.gracedev.expensetracker.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gracedev.expensetracker.databinding.BudgetItemLayoutBinding
import com.gracedev.expensetracker.model.Budget

class BudgetListAdapter(private val budgetList: ArrayList<Budget>)
    : RecyclerView.Adapter<BudgetListAdapter.BudgetViewHolder>() {

    class BudgetViewHolder(val binding: BudgetItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = BudgetItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.binding.txtName.text = budgetList[position].name
        holder.binding.txtNominal.text=budgetList[position].budget
    }

    override fun getItemCount(): Int = budgetList.size

    fun updateTodoList(newTodoList: List<Budget>) {
        budgetList.clear()
        budgetList.addAll(newTodoList)
        notifyDataSetChanged()
    }
}