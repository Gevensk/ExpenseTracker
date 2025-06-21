package com.gracedev.expensetracker.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gracedev.expensetracker.databinding.ExpenseItemLayoutBinding
import com.gracedev.expensetracker.model.Expense

class ExpenseListAdapter(private val expenseList: ArrayList<Expense>) :
    RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(val binding: ExpenseItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

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
        holder.binding.chipBudget.text = "ID: ${expense.budgetId}"
        // Bisa diganti jika ada notes/keterangan

        // Bisa tambahkan event onClick jika mau ada aksi edit/hapus nanti
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
