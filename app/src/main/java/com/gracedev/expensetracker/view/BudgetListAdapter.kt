package com.gracedev.expensetracker.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.gracedev.expensetracker.databinding.BudgetItemLayoutBinding
import com.gracedev.expensetracker.model.Budget

class BudgetListAdapter(private val budgetList: ArrayList<Budget>)
    : RecyclerView.Adapter<BudgetListAdapter.BudgetViewHolder>() {

    class BudgetViewHolder(val binding: BudgetItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        var binding = BudgetItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.binding.txtName.text = budgetList[position].name
        holder.binding.txtNominal.text=budgetList[position].budget.toString()

        holder.binding.txtName.setOnClickListener {
            val action =
                BudgetListFragmentDirections.actionEditBudgetFragment(budgetList[position].uuid)

            Navigation.findNavController(it).navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return budgetList.size
    }


    fun updateTodoList(newTodoList: List<Budget>) {
        budgetList.clear()
        budgetList.addAll(newTodoList)
        notifyDataSetChanged()
    }
}